package org.example.st.noel.pa


import st.noel.pa.api.Controllers.UserController
import st.noel.pa.api.Framework.Core.ControllerRegistry
import st.noel.pa.api.Server.AppServer

fun main() {

    ControllerRegistry.register(UserController())
    /*val path = "/api/users/ative"
    val queryParams = emptyMap<String, String>()
    val result = ControllerRegistry1.resolveWithParams(path, queryParams)
    if (result != null) {
        val json = result.toJsonString() // <-- precisas de converter JsonValue em String!
        //sendHttpResponse(200, json) // esta parte depende do teu motor HTTP
    } else {
        //sendHttpResponse(404, "Not Found")
    }*/
    AppServer.start(8087)
}


