package com.fox.p2p

import com.fox.persistence.UsersRepository
import com.fox.user.PersistableUser
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec

/**
 * Created by Stephen Fox on 3/24/19.
 */
class SendMoneySpecification : FunSpec({

    context("Receiver accepts") {

        val usersRepository = UsersRepository()
        val service = SendMoneyService(usersRepository)

        test("Two brand new users, one sends money to the other") {

            val user1 = PersistableUser.inMemory(100.0)
            val user2 = PersistableUser.inMemory(100.0)
            usersRepository.register(user1, user2)

            with(service) {
                // verifying all state in one go.
                user1.pendingTransactions(user2).count() shouldBe 0

                val request = user1.send(user2, someMoney = 10.0)

                user1.pendingTransactions(user2).count() shouldBe 1

                user2.accept(request)

                usersRepository[user1.id].balance shouldBe user1.balance - request.amount
                usersRepository[user2.id].balance shouldBe user2.balance + request.amount

                user1.pendingTransactions(user2).count() shouldBe 0
            }
        }
    }

    context("Receiver rejects") {

        val usersRepository = UsersRepository()
        val service = SendMoneyService(usersRepository)

        test("Two brand new users, one sends money to the other") {

            val user1 = PersistableUser.inMemory(100.0)
            val user2 = PersistableUser.inMemory(100.0)
            usersRepository.register(user1, user2)

            with(service) {
                // verifying all state in one go.
                user1.pendingTransactions(user2).count() shouldBe 0

                val request = user1.send(user2, someMoney = 10.0)

                user1.pendingTransactions(user2).count() shouldBe 1

                user2.reject(request)

                usersRepository[user1.id].balance shouldBe user1.balance
                usersRepository[user2.id].balance shouldBe user2.balance

                user1.pendingTransactions(user2).count() shouldBe 0
            }
        }
    }
})