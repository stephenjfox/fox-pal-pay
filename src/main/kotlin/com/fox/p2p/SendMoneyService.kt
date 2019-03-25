package com.fox.p2p

import com.fox.money.Money
import com.fox.money.minus
import com.fox.money.plus
import com.fox.persistence.RequestsRepository
import com.fox.persistence.UsersRepository
import com.fox.user.PersistableUser

class SendMoneyService(
    private val usersRepository: UsersRepository,
    private val requestsRepository: RequestsRepository = RequestsRepository()
) {

    fun PersistableUser.accept(request: SendMoneyRequest) {
        return this@SendMoneyService.acceptFor(this, request)
    }

    fun PersistableUser.pendingTransactions(toUser: PersistableUser): Sequence<SendMoneyRequest> {
        return this@SendMoneyService.requestsRepository.transactionsFor(this).filter { it.receiver == toUser }
    }

    fun PersistableUser.reject(request: SendMoneyRequest) {
        this@SendMoneyService.deleteRequest(this, request)
    }

    fun PersistableUser.send(otherUser: PersistableUser, someMoney: Money): SendMoneyRequest {
        return this@SendMoneyService.createRequest(fromUser = this, toUser = otherUser, amount = someMoney)
    }

    private fun acceptFor(receivingUser: PersistableUser, request: SendMoneyRequest) {
        val senderUpdate = request.sender - request.amount
        val receiverUpdate = receivingUser + request.amount

        usersRepository[receivingUser.id] = receiverUpdate
        usersRepository[request.sender.id] = senderUpdate

        requestsRepository.complete(request, receivingUser.id)
    }

    private fun createRequest(
        fromUser: PersistableUser,
        toUser: PersistableUser,
        amount: Money
    ): SendMoneyRequest {
        val request = SendMoneyRequest(amount, sender = fromUser, receiver = toUser)
        requestsRepository.track(request)
        return request
    }

    private fun deleteRequest(forUser: PersistableUser, request: SendMoneyRequest) {
        requestsRepository.complete(request, forUser.id)
    }
}
