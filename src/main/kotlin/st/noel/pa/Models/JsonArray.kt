package org.example.st.noel.pa.Models

import org.example.st.noel.pa.Interfaces.JsonValue
import org.example.st.noel.pa.Interfaces.JsonVisitor

/**
 * O JSONArray, armazenando uma lista mutável de elementos [JsonValue].
 *
 * Implementa a interface [JsonValue] para permitir serialização e o padrão Visitor,
 * além de implementar [Iterable] para suportar iteração dos elementos.
 *
 * Suporta operações funcionais como map, filter e remove elementos duplicados.
 *
 * @property elements, lista de elementos internos do JsonArray.
 */
data class JsonArray(
    private val elements: MutableList<JsonValue> = mutableListOf()
) : JsonValue, Iterable<JsonValue> {
    /**
     * Retorna um iterador sobre os elementos do array.
     */
    override fun iterator(): Iterator<JsonValue> {
        return elements.iterator()
    }
    /**
     * Aplica o padrão Visitor ao array para todos os elementos.
     *
     * @param visitor O visitante a ser aplicado.
     */
    override fun accept(visitor: JsonVisitor) {
        //aplicação do visitor em todos os filhos para elementos
        visitor.visit(this)
        elements.forEach{
            value -> value.accept(visitor)
        }
    }
    /**
     * Converte o JsonArray em uma string do JSON válido.
     *
     * @return A string com JSON do array.
     */
    override fun toJsonString(): String {
        /* verificar o jointoString usado na sala de aula*/
        return "[${elements.joinToString(",") {it.toJsonString() }}]"
    }
    /**
     * Retorna o elemento presente no índice fornecido.
     *
     * @param index A posição do elemento desejado.
     * @return O valor JSON naquela posição.
     */
    fun get(index: Int): JsonValue = elements.get(index)
    /**
     * Adiciona um novo elemento ao array JSON.
     *
     * @param value O valor JSON a ser adicionado.
     */
    fun addElmentArray(value: JsonValue) = elements.add(value)
    /*
     Variavel para obter o tamanho do array Json
     *@return o número de elementos presentes no array
     */
    val size: Int get() = elements.size
    /**
     * Filtra os elementos do array com base no paramêtro.
     *
     * @param value Função que define a condição de filtragem.
     * @return Um novo [JsonArray] contendo os elementos que satisfaça a condição fornecida.
     */
    fun filterArray(value: (JsonValue) -> Boolean): JsonArray{
        return JsonArray(elements.filter(value).toMutableList())
    }
    /**
     * Aplica a transformação a cada elemento do array.
     *
     * @param transform Função de transformação.
     * @return Um novo [JsonArray] com os elementos transformados.
     */
    fun <T: JsonValue> map(transform: (JsonValue)->T): JsonArray{
        return JsonArray(elements.map(transform).toMutableList())
    }
    /**
     * Remove elementos duplicados no JsonArray.
     *
     * @return o novo [JsonArray] contendo elementos únicos.
     */
    fun removeDuplicateElements(): JsonArray{
        val seen = mutableListOf<String>()
        val filtered = elements.filter {
            val str = it.toJsonString()
            if (seen.contains(str)){
                false
            }else{
                seen.add(str)
                true
            }
        }
        return JsonArray(filtered.toMutableList())
    }

}