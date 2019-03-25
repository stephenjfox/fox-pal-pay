package com.fox.persistence

import com.fox.p2p.MoneyEntreatyRequest
import com.fox.p2p.MoneyRequest
import com.fox.p2p.SendMoneyRequest
import com.fox.user.PersistableUser
import com.fox.user.UserId
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.collections.LinkedHashSet

/**
 * Created by Stephen Fox on 3/22/19.
 */
class RequestsRepository {

    private val moneyEntreatyRequests: MutableList<MoneyEntreatyRequest> = LinkedList()
    private val sendMoneyRequests: MutableList<SendMoneyRequest> = LinkedList()

    private val completedRequests = DefaultDict<UserId, MutableSet<MoneyRequest>> { LinkedHashSet() }

    /**
     * Get all of the requests that are targeted at [user].
     * I.e. if people want money from you, you are [user]
     */
    fun entreatiesFor(user: PersistableUser): Sequence<MoneyEntreatyRequest> {
        return moneyEntreatyRequests.asSequence().filter { it.entreatant == user }
    }

    fun track(request: MoneyRequest) = when (request) {
        is SendMoneyRequest -> sendMoneyRequests.add(request)
        is MoneyEntreatyRequest -> moneyEntreatyRequests.add(request)
        else -> error("Bad instance of MoneyRequest $request")
    }

    /**
     * Every request that [user] has to send money to someone.
     */
    fun transactionsFor(user: PersistableUser): Sequence<SendMoneyRequest> {
        return sendMoneyRequests.asSequence().filter { it.sender == user }
    }

    /**
     * Completes a [request] for a user, identified by [userId], such that the request cannot
     * be re-executed.
     * A new request should be generated for a future transaction
     */
    fun complete(request: MoneyRequest, userId: UserId) {
        val completedRequestProcessing = completedRequests[userId].add(request)
        if (!completedRequestProcessing) {
            error("Completing a request for a user not involved in the transaction")
        }
        removeFromBackingStore(request)
    }

    private fun removeFromBackingStore(request: MoneyRequest) {
        when (request) {
            is MoneyEntreatyRequest -> moneyEntreatyRequests.remove(request)
            is SendMoneyRequest -> sendMoneyRequests.remove(request)
            else -> error("Tried to remove unsupported request of type ${request.javaClass.canonicalName}")
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