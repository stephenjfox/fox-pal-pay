package com.fox.web

import org.http4k.core.*
import org.http4k.core.Status.Companion.OK
import org.http4k.filter.ServerFilters
import org.http4k.routing.bind
import org.http4k.routing.routes

fun Http4kAppServer(): HttpHandler = ServerFilters.CatchLensFailure.then(
    // TODO: move this under "/admin"
    routes(
        "/create" bind Method.GET to { _: Request -> Response(OK) }
    )
)