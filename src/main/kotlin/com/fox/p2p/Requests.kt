package com.fox.p2p

import com.fox.money.Money
import com.fox.user.PersistedUser

/**
 * Created by Stephen Fox on 3/16/19.
 */

/**
 * Marker interface. May be subject to deletion should it be excessive
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
    val receiver: PersistedUser,
    /**
     * The [entreatant] is the user from whom the [amount] will be deducted
     */
    val entreatant: PersistedUser
): MoneyRequest
