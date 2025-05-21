package org.example.st.noel.pa


import st.noel.pa.api.Controllers.UserController
import st.noel.pa.api.Framework.Core.ControllerRegistry
import st.noel.pa.api.Server.AppServer

fun main() {
    ControllerRegistry.register(UserController())
    AppServer.start()
}


