package st.noel.pa.api.Framework.Core

import org.example.st.noel.pa.Interfaces.JsonValue
import org.example.st.noel.pa.api.Annotation.GetMapping
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation

object ControllerRegistry1 {
    // Alterado: usamos um Pair<String, Regex> como chave para matching com variáveis
    private val routeMap = mutableListOf<RegisteredRoute>()

    private data class RegisteredRoute(
        val rawPath: String,
        val regex: Regex,
        val paramNames: List<String>,
        val controller: Any,
        val method: kotlin.reflect.KFunction<*>
    )

    fun register(vararg controllers: Any) {
        for (controller in controllers) {
            val kClass = controller::class
            for (method in kClass.declaredMemberFunctions) {
                val get = method.findAnnotation<GetMapping>() ?: continue
                val (regex, paramNames) = convertPathToRegex(get.path)
                routeMap.add(RegisteredRoute(get.path, regex, paramNames, controller, method))
            }
        }
    }

    private fun convertPathToRegex(path: String): Pair<Regex, List<String>> {
        val regex = StringBuilder("^")
        val paramNames = mutableListOf<String>()

        path.split("/").forEach {
            when {
                it.startsWith("{") && it.endsWith("}") -> {
                    val param = it.substring(1, it.length - 1)
                    regex.append("/([^/]+)")
                    paramNames.add(param)
                }
                it.isNotBlank() -> regex.append("/$it")
            }
        }

        regex.append("/?$")
        return Regex(regex.toString()) to paramNames
    }

    fun resolveWithParams(path: String, queryParams: Map<String, String>): JsonValue? {
        for (route in routeMap) {
            val match = route.regex.matchEntire(path) ?: continue
            val pathValues = match.groupValues.drop(1)

            val pathParams = route.paramNames.zip(pathValues).toMap()

            val allParams = pathParams + queryParams

            val args: Map<KParameter, Any?> = route.method.parameters
                .filter { it.kind == KParameter.Kind.VALUE }
                .associateWith { param ->
                    val name = param.name ?: return@associateWith null
                    val value = allParams[name]
                    when (param.type.classifier) {
                        Int::class -> value?.toIntOrNull() ?: 0
                        Double::class -> value?.toDoubleOrNull() ?: 0.0
                        Boolean::class -> value?.toBooleanStrictOrNull() ?: false
                        String::class -> value ?: ""
                        else -> null
                    }
                }

            return route.method.callBy(mapOf(route.method.parameters.first() to route.controller) + args) as? JsonValue
        }

        return null
    }
}