package com.fox.p2p

import com.fox.money.Money
import com.fox.persistence.RequestsRepository
import com.fox.user.User

/**
 * Created by Stephen Fox on 3/22/19.
 *
 * The service for handling requests between two users. Imagine:
 * 1. User A wants $10 from User B
 * 2. User B knows they owe lunch to User A
 * 3. User B accepts/confirms the request and transaction begins
 * 4. User A receives the funds, closing the transaction
 *
 * No funds are kept in-flight
 * - Transactions are not re-tried, because that would be done at a systems-level
 * -
 */
class MoneyEntreatyService(private val requestsRepository: RequestsRepository = RequestsRepository()) {
    /**
     * filter the transactions for the request that [requester] made to [entreated]
     * and begin the computation.
     */
    fun createRequest(requester: User, entreated: User, amount: Money): MoneyEntreatyRequest {
        // TODO: tracking users via some identification system.
        // Maybe a persisted extension class that just delegates...
        val request = MoneyEntreatyRequest(amount, receiver = requester, entreatant = entreated)

        requestsRepository.track(request)

        return request
    }

    private fun acceptFor(user: User, request: MoneyEntreatyRequest) {
        TODO("Should transition the $request into a 'completed' state, no longer allowing processing")
    }

    /*
    ================================================================================
    Extension functions:
    * We use E.F. to simplify the API, and give a nice kind of readability
    * It also embodies the "Open-Closed" Principle of the SOLID principles
    ================================================================================
     */

    /**
     * The requests from [this] that haven't yet been responded to by [forUser]
     */
    fun User.pendingRequests(forUser: User): Sequence<MoneyEntreatyRequest> {
        return this@MoneyEntreatyService.requestsRepository.entreatiesFor(forUser).filter { it.receiver == this }
    }

    /**
     * [this] accepts the [request] for money, thus initiating the transfer
     */
    fun User.accept(request: MoneyEntreatyRequest) {
        this@MoneyEntreatyService.acceptFor(this, request)
    }
}