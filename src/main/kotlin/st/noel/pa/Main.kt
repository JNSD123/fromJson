package org.example.st.noel.pa

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
//import io.ktor.server.plugins.callloging.*

fun main() {
    println("olá mundo!")
    embeddedServer(Netty, port = 8087, host = "0.0.0.0") {
        //install(CallLogging)

        routing {
            get("/") {
                call.respondText("Hello from Ktor!")
            }

            get("/json") {
                call.respond(mapOf("message" to "Hello, JSON!"))
            }
        }
    }.start(wait = true)
}


