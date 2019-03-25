package com.fox.user

import io.kotlintest.matchers.types.shouldBeSameInstanceAs
import io.kotlintest.shouldBe
import io.kotlintest.specs.FeatureSpec

class PersistableUserSpecification : FeatureSpec({

    feature("Generated id") {
        scenario("Created with a unique id") {
            val user = PersistableUser.inMemory(10.0)
            user.id shouldBeSameInstanceAs user.id
        }
        scenario("Id is the same across accesses") {
            val user = PersistableUser.inMemory(20.0)

            user.id.toString() shouldBe user.id.toString()
        }
    }

})