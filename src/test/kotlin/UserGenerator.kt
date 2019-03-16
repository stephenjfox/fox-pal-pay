import com.fox.User
import io.kotlintest.properties.Gen

/**
 * Generate [User]s to test
 */
class UserGenerator: Gen<User> {
    override fun constants() = emptyList<User>()

    override fun random() = generateSequence {
        User(Gen.double().random().first())
    }
}