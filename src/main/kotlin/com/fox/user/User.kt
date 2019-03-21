package com.fox.user

/**
 * Created by Stephen Fox on 3/16/19.
 */

/**
 * An lazy, delegating type that loads data when necessary, rather than
 * being a dumb data carrier that needs orchestration.
 * Objects can be smart
 */
class User private constructor(val balance: Double) {

    companion object {

        fun inMemory(balance: Double): User = when {
            balance.isNan() -> User(0.0)
            balance < 0 -> User(0.0)
            else -> User(balance)
        }

        private fun Double.isNan() = this == (Double.NaN as Number)

    }

}