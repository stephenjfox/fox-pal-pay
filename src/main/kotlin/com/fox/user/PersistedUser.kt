package com.fox.user

import com.fox.money.Money
import java.util.*

/**
 * Created by Stephen Fox on 3/16/19.
 */

typealias UserId = UUID

interface IUser {
    val balance: Money

    companion object
}

/**
 * An lazy, delegating type that loads data when necessary, rather than
 * being a dumb data carrier that needs orchestration.
 * Objects can be smart
 */
class PersistedUser
private constructor(
    override val balance: Money,
    val id: UserId = lazy { UserId.randomUUID() }.value
) : IUser {

    companion object {

        fun inMemory(balance: Money): PersistedUser = when {
            balance.isNan() -> PersistedUser(0.0)
            balance < 0 -> PersistedUser(0.0)
            else -> PersistedUser(balance)
        }

        private fun Money.isNan() = this == (Money.NaN as Number)
    }

    override fun toString(): String {
        return "User(balance=$balance, id=${id.toString().take(4)})"
    }
}

data class User(val balance: Money)