package st.noel.pa.api.Web

import com.sun.net.httpserver.HttpExchange
import org.example.st.noel.pa.Inference.JsonInferrer
import kotlin.reflect.KFunction
import kotlin.reflect.KType

class RouteHandler(
    private val instance: Any,
    private val function: KFunction<*>
) {
    fun call(exchange: HttpExchange): Any{
       return try {
            val params = function.parameters.drop(1).map { param ->
                val query = exchange.requestURI.query.orEmpty()
                val map = query.split("&").associate {
                    val (k, v) = it.split("=")
                    k to v
                }
                val value = map[param.name] ?: ""
                castToParamType(value, param.type)
            }

            val result = function.call(instance, *params.toTypedArray())

            val json = JsonInferrer.infer(result)
            val jsonString = json.toJsonString()

            exchange.responseHeaders.add("Content-Type", "application/json")
            val bytes = jsonString.toByteArray()
            exchange.sendResponseHeaders(200, bytes.size.toLong())
            exchange.responseBody.use {
                it.write(bytes)
            }
        }catch (e: Exception){
            val errorJson = JsonInferrer.infer(mapOf("error" to (e.message ?: "Erro interno")))
            val jsonString = errorJson.toJsonString()
            val bytes = jsonString.toByteArray()
            exchange.responseHeaders.add("Content-Type", "application/json")
            exchange.sendResponseHeaders(500, bytes.size.toLong())
            exchange.responseBody.use {
                it.write(bytes)
            }
        }

    }
    private fun castToParamType(value: String, type: KType): Any {
        return when (type.classifier) {
            Int::class -> value.toInt()
            Boolean::class -> value.toBoolean()
            Double::class -> value.toDouble()
            else -> value // trata como String por padrão
        }
    }
}