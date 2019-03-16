package com.fox

/**
 * Created by Stephen Fox on 3/16/19.
 */
class SendMoneyService {

    companion object {
        fun User.sendMoney(to: User): SendMoneyRequest {
            return SendMoneyRequest()
        }
    }

}

fun User.deny(request: MoneyRequest) {
    // TODO: implement the denial at the storage layer
}

class RequestMoneyService {

    companion object {

        fun User.requestMoney(from: User): RequestMoneyEntreaty {
            return RequestMoneyEntreaty()
        }

    }

}