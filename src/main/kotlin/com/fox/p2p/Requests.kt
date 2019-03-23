package com.fox.p2p

import com.fox.money.Money
import com.fox.user.User

/**
 * Created by Stephen Fox on 3/16/19.
 */

interface MoneyRequest

data class MoneyEntreatyRequest(
    /**
     * The [amount] of [Money] requested by [receiver]
     */
    val amount: Money,
    /**
     * One whose balance will increase as a result of the [entreatant] accepting the request
     */
    val receiver: User,
    /**
     * The [entreatant] is the user from whom the [amount] will be deducted
     */
    val entreatant: User
): MoneyRequest
