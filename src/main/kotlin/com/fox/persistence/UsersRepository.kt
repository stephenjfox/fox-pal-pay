package com.fox.persistence

import com.fox.user.PersistedUser
import com.fox.user.User
import com.fox.user.UserId

/**
 * Created by Stephen Fox on 2019-03-24.
 *
 * A shallow wrapper over a [Map], intended to give a constrained view of the
 * contained data.
 * No support for nulls, which demands that created Users be registered with this instance
 * or an [IllegalStateException] will occur
 */
class UsersRepository {

    private val backingStore = mutableMapOf<UserId, User>()

    operator fun set(id: UserId, userData: User) {
        backingStore[id] = userData
    }

    operator fun get(id: UserId): User {
        return backingStore[id] ?: error("Attempted to 'get' a non-register user")
    }

    fun register(vararg persistedUsers: PersistedUser) {
        for (persistedUser in persistedUsers) {
            backingStore[persistedUser.id] = persistedUser.toUser()
        }
    }
}

private fun PersistedUser.toUser() = User(this.balance)
