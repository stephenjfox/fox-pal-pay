package com.fox.persistence

import com.fox.p2p.MoneyEntreatyRequest
import com.fox.user.PersistedUser
import com.fox.user.UserId
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.collections.LinkedHashSet

/**
 * Created by Stephen Fox on 3/22/19.
 */
class RequestsRepository {

    private val moneyEntreatyRequests: MutableList<MoneyEntreatyRequest> = LinkedList()

    private val completedRequests = DefaultDict<UserId, MutableSet<MoneyEntreatyRequest>> { LinkedHashSet() }

    /**
     * Get all of the requests that are targeted at [user].
     * E.g. if people want money from you, you are [user]
     */
    fun entreatiesFor(user: PersistedUser): Sequence<MoneyEntreatyRequest> {
        return this.moneyEntreatyRequests.asSequence().filter { it.entreatant == user }
    }

    fun track(request: MoneyEntreatyRequest) = moneyEntreatyRequests.add(request).also {
        println("Added a request: $moneyEntreatyRequests")
    }

    /**
     * Completes a [request] for a user, identified by [userId], such that the request cannot
     * be re-executed.
     * A new request should be generated for a future transaction
     */
    fun complete(request: MoneyEntreatyRequest, userId: UserId) {
        val completedRequestProcessing = completedRequests[userId].add(request)
        if (completedRequestProcessing) {
            moneyEntreatyRequests.remove(request)
        } else {
            error("Completing a request for a user not involved in the transaction")
        }
    }
}

/**
 * Meant to simulate Python's defaultdict(). This abstraction may be unnecessary, but its
 * intent is to keep clean the other parts of the module.
 * Should it be found useful elsewhere, it will be made public.
 * Should it be deemed unnecessary, its implementations will be in-lined and this class removed.
 */
private class DefaultDict<K, V>(private val generateDefault: () -> V) {

    private val map = LinkedHashMap<K, V>()

    operator fun get(key: K): V {
        if (key !in map) map[key] = generateDefault()
        return map[key]!!
    }
}