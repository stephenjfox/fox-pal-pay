package com.fox.persistence

import com.fox.p2p.MoneyEntreatyRequest
import com.fox.user.User
import java.util.*

/**
 * Created by Stephen Fox on 3/22/19.
 */
class RequestsRepository {

    /**
     * TODO: make this a map of requester: User => request: MoneyEntreatyRequest.
     */
    private val moneyEntreatyRequests: MutableList<MoneyEntreatyRequest> = LinkedList()

    /**
     * Get all of the requests that are targeted at [user].
     * E.g. if people want money from you, you are [user]
     */
    fun entreatiesFor(user: User): Sequence<MoneyEntreatyRequest> {
        return this.moneyEntreatyRequests.asSequence().filter { it.entreatant == user }
    }

    fun entreatiesFrom(user: User): Sequence<MoneyEntreatyRequest> {
        return this.moneyEntreatyRequests.asSequence().filter { it.receiver == user }
    }

    fun track(request: MoneyEntreatyRequest) = moneyEntreatyRequests.add(request).also {
        println("Added a request: $moneyEntreatyRequests")
    }
}