package org.example.st.noel.pa.Validator

import org.example.st.noel.pa.Interfaces.JsonValue
import org.example.st.noel.pa.Interfaces.JsonVisitor
import org.example.st.noel.pa.Models.*

/**
 * Validador de tipos homogêneos em arrays JSON.
 *
 * Implementa o padrão Visitor para percorrer estruturas JSON e verificar se os arrays contêm elementos de tipos mistos.
 * Caso um array contenha múltiplos tipos primitivos (e.g., `string`, `number`), um erro de validação será registrado.
 */
object JsonArrayTypeValidator: JsonVisitor {
    /** Lista de erros encontrados durante a validação dos tipos. */
    private val validationErros = mutableListOf<String>()
    /**
     * Pilha de índices que representa o caminho atual dentro dos arrays visitados.
     * Usada para rastrear a posição de arrays aninhados.
     */
    private val pathStack = mutableListOf<Int>()
    /**
     * Conjunto de arrays já visitados para evitar ciclos ou repetições desnecessárias.
     */
    private val visitedArrays = mutableSetOf<JsonArray>()

    /**
     * Executa a validação de tipos homogêneos em arrays dentro de uma estrutura JSON.
     *
     * @param json Estrutura JSON a ser validada.
     * @return Lista de mensagens de erro indicando arrays com múltiplos tipos.
     */
    fun validateArrayTypeValidator(json: JsonValue): List<String>{
        validationErros.clear()
        pathStack.clear()
        visitedArrays.clear()
        json.accept(this)
        return validationErros.toList()
    }
    /**
     * Visita um [JsonArray] e verifica se contém múltiplos tipos distintos.
     * O JsonNull é ignorado na verificação dos tipos
     * @param arr O array JSON a ser visitado.
     */
    override fun visit(arr: JsonArray){
        if (visitedArrays.contains(arr)) return
        visitedArrays.add(arr)
        val types = arr.mapNotNull {
            when(it){
                is JsonString -> "string"
                is JsonNumber -> "number"
                is JsonBoolean -> "boolean"
                is JsonArray -> "array"
                is JsonObject -> "object"
                is JsonNull -> null
                else -> null
            }
        }.toSet()
        if (types.size > 1){
            val pathStr = if (pathStack.isEmpty()) "[]" else
                pathStack.joinToString(separator = "][", prefix = "[", postfix = "]")
            validationErros.add("Array na posição $pathStr contém diferentes tipos: ${types.sorted().joinToString()}")
        }
        arr.forEachIndexed{
            index, jsonValue ->
            when(jsonValue){
                is JsonArray ->{
                    pathStack.add(index)
                    jsonValue.accept(this)
                    pathStack.removeLast()
                }
                is JsonObject ->jsonValue.accept(this)
                else -> {}
            }
        }
    }
    /**
     * Visita um [JsonObject] e aplica recursivamente a validação aos seus valores.
     *
     * @param obj O objeto JSON a ser visitado.
     */
    override fun visit(obj: JsonObject) {
        obj.properties.forEach {
            (_, values)->values.accept(this)
        }
    }
    /** Visita um [JsonString]. Não realiza nenhuma ação. */
    override fun visit(str: JsonString) {}
    /** Visita um [JsonNumber]. Não realiza nenhuma ação. */
    override fun visit(numb: JsonNumber) {}
    /** Visita um [JsonBoolean]. Não realiza nenhuma ação. */
    override fun visit(bool: JsonBoolean) {}
    /** Visita um [JsonNull]. Não realiza nenhuma ação. */
    override fun visit(nul: JsonNull) {}
    /** Visita a propriedade de [JsonArray]. Não realiza nenhuma ação. */
    override fun visitProperty(str: String, value: JsonValue) {}
}