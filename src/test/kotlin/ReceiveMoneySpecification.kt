import com.fox.p2p.MoneyEntreatyService
import com.fox.user.User
import io.kotlintest.matchers.doubles.shouldBeExactly
import io.kotlintest.matchers.numerics.shouldNotBeLessThan
import io.kotlintest.specs.FeatureSpec

/**
 * Created by Stephen Fox on 3/21/19.
 */
class ReceiveMoneySpecification : FeatureSpec({

    feature("User.accept") {

        val service = MoneyEntreatyService()
        scenario("The receiver accepts the request, the sender has the funds for the transaction") {
            val user1 = User.inMemory(0.0)
            val user2 = User.inMemory(20.0)
            val initialBalance = user1.balance
            val request = service.createRequest(requester = user1, entreated = user2, amount = 10.0)
            with(service) {
                user2.accept(request)
            }

            user1.balance shouldBeExactly initialBalance + request.amount
        }
        scenario("The receiver has a request out to the sender") {
            val user1 = User.inMemory(0.0)
            val user2 = User.inMemory(20.0)
            service.createRequest(requester = user1, entreated = user2, amount = 10.0) // set our state/precondition

            with(service) {
                user1.pendingRequests(forUser = user2).count() shouldNotBeLessThan 1
                user2.accept(createRequest(requester = user1, entreated = user2, amount = 10.0))
            }
        }
    }

    feature("User.reject") {
        scenario("The receiver rejects, the sender has funds for the transaction")
        scenario("The receiver rejects") {}
        scenario("The receiver has no pending requests with the sender") {}
        // TODO
    }

    feature("Failure cases") {
        // TODO: test when subsystems "go down"
    }

})