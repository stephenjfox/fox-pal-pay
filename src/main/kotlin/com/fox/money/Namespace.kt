package com.fox.money

import com.fox.user.PersistableUser
import com.fox.user.User

/**
 * Created by Stephen Fox on 3/20/19.
 */
typealias Money = Double

operator fun PersistableUser.minus(amount: Money): User {
    return User(this.balance - amount)
}

operator fun PersistableUser.plus(amount: Money): User {
    return User(this.balance + amount)
}
