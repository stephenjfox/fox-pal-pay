package com.fox.user

import io.kotlintest.matchers.types.shouldBeSameInstanceAs
import io.kotlintest.properties.Gen
import io.kotlintest.shouldBe
import io.kotlintest.specs.FeatureSpec

/**
 * Generate [PersistedUser]s to test
 */
class UserGenerator: Gen<PersistedUser> {
    override fun constants() = emptyList<PersistedUser>()

    override fun random() = generateSequence {
        PersistedUser.inMemory(Gen.double().random().first())
    }
}

class PersistedUserSpecification: FeatureSpec({

    feature("Generated id") {
        scenario("Created with a unique id") {
            val user = PersistedUser.inMemory(10.0)
            user.id shouldBeSameInstanceAs user.id
        }
        scenario("Id is the same across accesses") {
            val user = PersistedUser.inMemory(20.0)

            user.id.toString() shouldBe user.id.toString()
        }
    }

})