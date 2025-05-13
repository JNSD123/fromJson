package st.noel.pa.api.Web

import com.sun.net.httpserver.*
import org.example.st.noel.pa.api.Annotation.GetMapping
import org.example.st.noel.pa.api.Annotation.RestController
import java.net.InetSocketAddress
import kotlin.reflect.full.*


class MiniHttpServer(
    private val port: Int
) {
    private val routes = mutableMapOf<String, RouteHandler>()

    fun registerControllers(vararg controllers: Any){
        for (controller in controllers){
            val kClass = controller::class
            if (!kClass.hasAnnotation<RestController>()) continue

            for (function in kClass.declaredMemberFunctions){
                val getAnnotation = function.findAnnotation<GetMapping>() ?: continue
                val path = getAnnotation.path
                routes[path] = RouteHandler(controller,function)
            }
        }
    }
    fun start(){
        val server = HttpServer.create(InetSocketAddress(port), 0)
        for ((path, handler) in routes){
            server.createContext(path){exchange ->
                if (exchange.requestMethod == "GET"){
                    val resutl = handler.call(exchange)
                }
            }
        }
    }
}