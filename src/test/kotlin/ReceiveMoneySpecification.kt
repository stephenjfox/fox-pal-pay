import com.fox.p2p.MoneyEntreatyService
import com.fox.persistence.UsersRepository
import com.fox.user.PersistedUser
import io.kotlintest.matchers.doubles.shouldBeExactly
import io.kotlintest.matchers.numerics.shouldNotBeLessThan
import io.kotlintest.shouldBe
import io.kotlintest.specs.FeatureSpec

/**
 * Created by Stephen Fox on 3/21/19.
 */
class ReceiveMoneySpecification : FeatureSpec({

    feature("User.accept()") {

        val usersRepository = UsersRepository()
        val service = MoneyEntreatyService(usersRepository)
        scenario("The receiver accepts the request, the sender has the funds for the transaction") {
            val user1 = PersistedUser.inMemory(0.0)
            val user2 = PersistedUser.inMemory(20.0)
            val initialBalance = user1.balance

            val request = with(service) {
                val request = user1.ask(user2, forAmount = 10.0)
                user2.accept(request)
                request
            }

            usersRepository[user1.id].balance shouldBeExactly initialBalance + request.amount
        }

        scenario("The receiver has a request out to the lender") {
            val user1 = PersistedUser.inMemory(0.0)
            val user2 = PersistedUser.inMemory(20.0)

            with(service) {
                // set our state/precondition
                user1.ask(user2, forAmount = 10.0)

                user1.pendingRequests(forUser = user2).count() shouldNotBeLessThan 1
                user2.accept(user1.ask(user2, forAmount = 10.0))
            }
        }
    }

    feature("User.reject()") {

        val usersRepository = UsersRepository()
        val service = MoneyEntreatyService(usersRepository)

        scenario("The receiver has a request out to the lender") {

            val user1 = PersistedUser.inMemory(100.0)
            val user2 = PersistedUser.inMemory(100.0)

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