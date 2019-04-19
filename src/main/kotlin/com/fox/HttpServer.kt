package com.fox

import com.fox.web.Http4kAppServer
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun main() {

    AppServer.start(8080)

}

object AppServer {
    fun start(port: Int) {
        // no need to detach from the thread at this point.
        // if we ever wanted asynchronous work just swap "block()" for "start()"
        Http4kAppServer().asServer(Jetty(port)).block()
    }
}
