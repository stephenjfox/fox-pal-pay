package com.fox.p2p

import com.fox.money.Money
import com.fox.money.minus
import com.fox.money.plus
import com.fox.persistence.RequestsRepository
import com.fox.persistence.UsersRepository
import com.fox.user.PersistableUser
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
 *
 *
 * ****************************** Functionality ******************************
 *
 * The API is kept <b>private</b> to make this a message-driven system (i.e. aligned original intent of OOP)
 * PersistedUsers send messages back and forth with function calls that act at the object level.
 * Objects are persisted at every step so that the programmer can think about objects, and not services,
 * when working in the codebase.
 */
class MoneyEntreatyService(
    private val usersRepository: UsersRepository,
    private val requestsRepository: RequestsRepository = RequestsRepository()
) {
    /**
     * Accept the request for money on behalf of the [persistableUser], deducting the [MoneyEntreatyRequest.amount]
     * from that user and giving it to the [MoneyEntreatyRequest.receiver]
     */
    private fun acceptFor(persistableUser: PersistableUser, request: MoneyEntreatyRequest) {

        val entreatantUpdate: User = persistableUser - request.amount
        val requesterUpdate: User = request.receiver + request.amount

        // persist the changes
        usersRepository[persistableUser.id] = entreatantUpdate
        usersRepository[request.receiver.id] = requesterUpdate

        // close the transaction
        requestsRepository.complete(request, persistableUser.id)
    }

    /**
     * filter the transactions for the request that [requester] made to [entreated]
     * and begin the computation.
     */
    private fun createRequest(
        requester: PersistableUser,
        entreated: PersistableUser,
        amount: Money
    ): MoneyEntreatyRequest {
        // Maybe a persisted extension class that just delegates...
        val request = MoneyEntreatyRequest(amount, receiver = requester, entreatant = entreated)

        requestsRepository.track(request)

        return request
    }

    /**
     * Removes the [request] for the supplied [persistableUser]
     */
    private fun deleteRequest(persistableUser: PersistableUser, request: MoneyEntreatyRequest) {
        // no transaction, just don't allow the request to be processed
        requestsRepository.complete(request, persistableUser.id)
    }


    /*
    ================================================================================
    Extension functions:
    * We use E.F. to simplify the API, and give a nice kind of readability
    * It also embodies the "Open-Closed" Principle of the SOLID principles
    ================================================================================
     */

    /**
     * The requests from [this] that haven't yet been responded to by [forUser].
     *
     * Meant to support the following DSL:
     * <code>
     *     val oldestRequest = user1.pendingRequests(forUser = otherUser).first()
     *     // or
     *     val requestsToBrittany = user.pendingRequests(forUser = brittany).count()
     * </code>
     */
    fun PersistableUser.pendingRequests(forUser: PersistableUser): Sequence<MoneyEntreatyRequest> {
        return this@MoneyEntreatyService.requestsRepository.entreatiesFor(forUser).filter { it.receiver == this }
    }

    /**
     * [this] accepts the [request] for money, thus initiating the transfer.
     *
     * Meant to support the following DSL:
     * <code>
     *     val request = someUser.ask(user, forAmount = 10.0)
     *     user.accept(request)
     * </code>
     */
    fun PersistableUser.accept(request: MoneyEntreatyRequest) {
        this@MoneyEntreatyService.acceptFor(this, request)
    }

    /**
     * [this] asks [user] [forAmount] of [Money]. There is an associated side-effect that persists
     * the created request. It is being tracked without burdening the user of this function.
     *
     * Meant to support the following DSL:
     * <code>
     *     user1.ask(user2, forAmount = 10.0)
     * </code>
     */
    fun PersistableUser.ask(user: PersistableUser, forAmount: Money): MoneyEntreatyRequest {
        return this@MoneyEntreatyService.createRequest(this, user, forAmount)
    }

    fun PersistableUser.reject(reject: MoneyEntreatyRequest) {
        this@MoneyEntreatyService.deleteRequest(this, reject)
    }

}
