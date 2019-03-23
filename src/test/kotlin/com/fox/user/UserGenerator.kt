package com.fox.user

import io.kotlintest.matchers.types.shouldBeSameInstanceAs
import io.kotlintest.properties.Gen
import io.kotlintest.shouldBe
import io.kotlintest.specs.FeatureSpec

/**
 * Generate [User]s to test
 */
class UserGenerator: Gen<User> {
    override fun constants() = emptyList<User>()

    override fun random() = generateSequence {
        User.inMemory(Gen.double().random().first())
    }
}

class UserSpecification: FeatureSpec({

    feature("Generated id") {
        scenario("Created with a unique id") {
            val user = User.inMemory(10.0)
            user.id shouldBeSameInstanceAs user.id
        }
        scenario("Id is the same across accesses") {
            val user = User.inMemory(20.0)

            user.id.toString() shouldBe user.id.toString()
        }
    }

})