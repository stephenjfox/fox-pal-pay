
import com.fox.RequestMoneyService.Companion.requestMoney
import com.fox.SendMoneyService.Companion.sendMoney
import com.fox.User
import com.fox.deny
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

/**
 * Created by Stephen Fox on 3/16/19.
 */
class RequestDenialSpecification : StringSpec() {

    init {
        "User should be able to reject send requests" {
            forAll(10, UserGenerator(), UserGenerator()) { user1: User, user2: User ->
                val user1Balance: Double = user1.balance
                val user2Balance = user2.balance

                val moneyRequest = user1.sendMoney(user2)
                user2.deny(moneyRequest)

                user1.balance == user1Balance && user2.balance == user2Balance
            }
        }

        "User should be able to reject money petitions" {
            forAll(10, UserGenerator(), UserGenerator()) { user1: User, user2: User ->
                val balance1 = user1.balance
                val balance2 = user2.balance

                val entreaty = user1.requestMoney(from = user2)
                user2.deny(entreaty)

                user1.balance == balance1 && user2.balance == balance2
            }
        }
    }

}

