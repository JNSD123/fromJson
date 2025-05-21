package st.noel.pa.api.Framework.Core

import org.example.st.noel.pa.Inference.JsonInferrer
import org.example.st.noel.pa.Interfaces.JsonValue
import org.example.st.noel.pa.api.Annotation.GetMapping
import org.example.st.noel.pa.api.Annotation.RestController
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation

/**
 * Registro central de controladores REST e seus endpoints, responsável por
 * armazenar rotas, fazer o mapeamento de URLs para métodos e resolver
 * chamadas HTTP com parâmetros de caminho e query.
 */
object ControllerRegistry {
    // Alterado: usamos um Pair<String, Regex> como chave para matching com variáveis
    /**
     * Lista interna que mantém as rotas registradas, cada uma associada a:
     * - rawPath: caminho original da rota
     * - regex: expressão regular para match da rota com parâmetros variáveis
     * - paramNames: nomes dos parâmetros da rota extraídos do caminho
     * - controller: instância do controlador onde o método está definido
     * - method: função a ser chamada para a rota
     */
    private val routeMap = mutableListOf<RegisteredRoute>()
    /**
     * Dados da rota registrada, incluindo a expressão regular para matching
     * e os nomes dos parâmetros dinâmicos da URL.
     */
    private data class RegisteredRoute(
        val rawPath: String,
        val regex: Regex,
        val paramNames: List<String>,
        val controller: Any,
        val method: kotlin.reflect.KFunction<*>
    )
    /**
     * Registra os controladores recebidos, inspecionando suas funções anotadas com [GetMapping].
     *
     * @param controllers Vararg de instâncias dos controladores a serem registrados.
     *
     * O caminho completo da rota é construído com o prefixo do [RestController] e o path do [GetMapping].
     * Rotas são convertidas para regex para suportar parâmetros dinâmicos.
     */
    fun register(vararg controllers: Any) {
        for (controller in controllers) {
            val kClass = controller::class
            val basePath = kClass.findAnnotation<RestController>()?.path?.trimEnd('/') ?: ""
//api/user
            for (method in kClass.declaredMemberFunctions) {
                val get = method.findAnnotation<GetMapping>() ?: continue
                val fullPath = (basePath + "/" + get.path.trimStart('/')).replace("//", "/")
                val (regex, paramNames) = convertPathToRegex(fullPath)
                routeMap.add(RegisteredRoute(fullPath, regex, paramNames, controller, method))
            }
        }
        routeMap.sortWith(compareBy({ it.paramNames.isNotEmpty() }, { it.rawPath.length }))
    }
    /**
     * Converte um path com parâmetros entre chaves `{param}` para uma expressão regular,
     * que captura os valores desses parâmetros e extrai seus nomes.
     *
     * @param path Caminho da rota, exemplo: "api/path/{pathvar}"
     * @return Par contendo a regex correspondente e a lista de nomes dos parâmetros.
     */
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
    /**
     * Resolve uma rota HTTP para o método registrado correspondente, preenchendo os parâmetros
     * de caminho e query com os valores extraídos da URL e chamando o método do controller.
     *
     * @param path Caminho da requisição HTTP.
     * @param queryParams Map dos parâmetros query da URL.
     * @return Objeto JSON para o Modelo(JsonArray, JsonObject, JsonString, JsonNumber, JsonBoolean, JsonNull) inferido pela inferência do [JsonInferrer] ao resultado do JSON final ou null se não encontrar rota.
     */
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

            val result = route.method.callBy(mapOf(route.method.parameters.first() to route.controller) + args)
            val jsonResult = JsonInferrer.infer(result)

            return jsonResult
        }
        return null
    }
}