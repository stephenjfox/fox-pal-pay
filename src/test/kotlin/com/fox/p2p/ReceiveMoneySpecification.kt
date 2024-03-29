package com.fox.p2p

import com.fox.persistence.UsersRepository
import com.fox.user.PersistableUser
import io.kotlintest.matchers.doubles.shouldBeExactly
import io.kotlintest.matchers.numerics.shouldNotBeLessThan
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec

/**
 * Created by Stephen Fox on 3/21/19.
 */
class ReceiveMoneySpecification : FunSpec({

    context("Receiver accepts") {

        val usersRepository = UsersRepository()
        val service = MoneyEntreatyService(usersRepository)

        test("the sender has the funds for the transaction") {
            val user1 = PersistableUser.inMemory(0.0)
            val user2 = PersistableUser.inMemory(20.0)
            val initialBalance = user1.balance

            val request = with(service) {
                val request = user1.ask(user2, forAmount = 10.0)
                user2.accept(request)
                request
            }

            usersRepository[user1.id].balance shouldBeExactly initialBalance + request.amount
        }

        test("While there is a pending request out to the lender") {
            val user1 = PersistableUser.inMemory(0.0)
            val user2 = PersistableUser.inMemory(20.0)

            with(service) {
                // set our state/precondition
                user1.ask(user2, forAmount = 10.0)

                user1.pendingRequests(forUser = user2).count() shouldNotBeLessThan 1
                user2.accept(user1.ask(user2, forAmount = 10.0))

                usersRepository[user2.id].balance shouldBeExactly user2.balance - 10.0
                usersRepository[user1.id].balance shouldBeExactly user1.balance + 10.0
            }
        }
    }

    context("Receiver rejects") {

        val usersRepository = UsersRepository()
        val service = MoneyEntreatyService(usersRepository)

        test("Brand new users: no transaction occurs") {
            val user1 = PersistableUser.inMemory(100.0)
            val user2 = PersistableUser.inMemory(100.0)

            usersRepository.register(user1, user2)

            with(service) {

                val request = user1.ask(user2, forAmount = 20.0)
                user1.pendingRequests(user2).count() shouldBe 1

                user2.reject(request)

                // balances should be unchanged
                usersRepository[user1.id].balance shouldBe user1.balance
                usersRepository[user2.id].balance shouldBe user2.balance
            }

        }

        test("Pending request out to the lender, no transaction occurs") {

            val user1 = PersistableUser.inMemory(100.0)
            val user2 = PersistableUser.inMemory(100.0)

            with(service) {
                // initial state: a pending request
                val initialRequest = user1.ask(user2, forAmount = 10.0)
                user1.pendingRequests(forUser = user2).count() shouldBe 1

                user2.reject(initialRequest)
                user1.pendingRequests(forUser = user2).count() shouldBe 0
            }
        }
    }

})