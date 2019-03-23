package com.fox.user

import com.fox.money.Money
import java.util.*

/**
 * Created by Stephen Fox on 3/16/19.
 */

typealias Identifier = UUID

interface IUser {
    val balance: Money

    companion object
}

/**
 * An lazy, delegating type that loads data when necessary, rather than
 * being a dumb data carrier that needs orchestration.
 * Objects can be smart
 */
class User
private constructor(override val balance: Money) : IUser {

    internal val id: Identifier = lazy { Identifier.randomUUID() }.value

    companion object {

        fun inMemory(balance: Money): User = when {
            balance.isNan() -> User(0.0)
            balance < 0 -> User(0.0)
            else -> User(balance)
        }

        private fun Money.isNan() = this == (Money.NaN as Number)
    }

    override fun toString(): String {
        return "User(balance=$balance, id=${id.toString().take(4)})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}