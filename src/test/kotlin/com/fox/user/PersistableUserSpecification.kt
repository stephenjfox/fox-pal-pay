package com.fox.user

import io.kotlintest.matchers.types.shouldBeSameInstanceAs
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec

class PersistableUserSpecification : FunSpec({

    test("Created with a unique id") {
        val user = PersistableUser.inMemory(10.0)
        user.id shouldBeSameInstanceAs user.id
    }
    test("Id is the same across accesses") {
        val user = PersistableUser.inMemory(20.0)

        user.id.toString() shouldBe user.id.toString()
    }

})