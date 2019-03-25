package com.fox.user

import com.fox.money.Money
import java.util.*

/**
 * Created by Stephen Fox on 3/16/19.
 */

typealias UserId = UUID

/**
 * Almost a marker interface. As these are egregious in code bases, unless they are
 * annotations, this may be deleted. In that case, we would just deal with classes
 */
interface IUser {
    val balance: Money

    companion object
}

/**
 * An lazy, delegating type that loads data when necessary, rather than
 * being a dumb data carrier that needs orchestration.
 */
class PersistableUser
private constructor(
    override val balance: Money,
    val id: UserId = UserId.randomUUID()
) : IUser {

    companion object {

        fun inMemory(balance: Money): PersistableUser = when {
            balance.isNan() -> PersistableUser(0.0)
            balance < 0 -> PersistableUser(0.0)
            else -> PersistableUser(balance)
        }

        private fun Money.isNan() = this == (Money.NaN as Number)
    }

    override fun toString(): String {
        return "User(balance=$balance, id=${id.toString().take(4)})"
    }
}

data class User(override val balance: Money) : IUser