package com.fox.p2p

import com.fox.money.Money
import com.fox.user.PersistableUser

/**
 * Created by Stephen Fox on 3/16/19.
 */

/**
 * Marker interface. May be subject to deletion should it be excessive
 */
interface MoneyRequest

/**
 * The [receiver] requests some [amount] of money from [entreatant]
 */
data class MoneyEntreatyRequest(
    val amount: Money,
    val receiver: PersistableUser,
    val entreatant: PersistableUser
) : MoneyRequest

/**
 * Represents the sending some [amount] of money from [sender] to [receiver]
 */
data class SendMoneyRequest(
    val amount: Money,
    val sender: PersistableUser,
    val receiver: PersistableUser
) : MoneyRequest