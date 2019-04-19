package com.fox

import org.http4k.core.*
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.ServerConfig

fun main() {

    val moneyApp = MoneyApp()
    moneyApp.start(Jetty(8080))
}

fun MoneyApp(): RoutingHttpHandler {
    val moneyApp = routes(
        "ping" bind Method.GET to { request: Request -> Response(Status.OK).body("Success") }
    )
    return moneyApp
}

// no need to detach from the thread at this point.
// if we ever wanted asynchronous work just swap "block()" for "start()"
fun HttpHandler.start(config: ServerConfig) = config.toServer(this).block()