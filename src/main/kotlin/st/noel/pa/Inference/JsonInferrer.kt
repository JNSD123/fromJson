package org.example.st.noel.pa.Inference


import org.example.st.noel.pa.Interfaces.JsonValue
import org.example.st.noel.pa.Models.*
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

/**
 * Objeto responsável por inferir automaticamente estruturas de dados Kotlin
 * (Int,Double,Boolean,String, List, map, enums, null, e data classes) para uma representação JSON
 * utiliza a hierarquia de tipos personalizados do modelos (JsonValue, JsonObject, JsonArray, JsonBoolean, JsonString,JsonNumber, JsonNull).
 */
object JsonInferrer {
    /**
     * Infere dinamicamente um objeto Kotlin e retorna a representação correspondente em JSON.
     *
     * @param obj O objeto a ser inferido.
     * @return Um [JsonValue] que representa o valor JSON equivalente ao objeto informado.
     *
     * @throws IllegalArgumentException mensagem de execessão quando a chave não for do tipo String.
     */
    fun infer(obj: Any?): JsonValue{
        return when(obj){
            null -> JsonNull
            is Boolean -> JsonBoolean(obj)
            is Number -> JsonNumber(obj)
            is String -> JsonString(obj)
            is Enum<*> -> JsonString(obj.name)
            is List<*> -> inferArray(obj)
            is Map<*, *> -> inferMap(obj.mapValues { infer(it.value) }.toMutableMap())
            else -> {
                val props = obj::class.memberProperties
                if (props.size == 1) {
                    // Unbox automático para data class com só uma propriedade
                    val singleValue = props.first().getter.call(obj)
                    infer(singleValue)
                } else {
                    inferDataClass(obj)
                }
            }
        }
    }
    /**
     * Converte uma lista Kotlin em um [JsonArray].
     *
     * @param list Lista a ser convertida.
     * @return O [JsonArray] contendo os elementos convertidos para JSON.
     */
    private fun inferArray(list: List<*>): JsonArray{
        val jsonArray = JsonArray()
        list.forEach{
                item -> jsonArray.addElmentArray(infer(item))
        }
        return jsonArray
    }
    /**
     * Converte um map Kotlin (com chaves do tipo String) em um [JsonObject].
     *
     * @param map Map a ser convertido.
     * @return O [JsonObject] representando o map em JSON.
     *
     * @throws IllegalArgumentException, mensagem de execessão quando a chave não for do tipo String.
     */
    private fun inferMap(map: Map<*,*>): JsonObject{
        val jsonObject = map.entries.map {
                (key, value) ->
            if (key !is String){
                throw IllegalArgumentException("A chave do mapeamento deve ser String")
            }
            key to infer(value)
        }
        return JsonObject(jsonObject)
    }
    /**
     * Converte uma data class Kotlin em um [JsonObject], inferindo automaticamente
     * os valores de cada propriedade.
     *
     * @param obj Instância da data class a ser convertida.
     * @return O [JsonObject] contendo os pares nome/valor das propriedades.
     */
    private fun inferDataClass(obj: Any): JsonObject{
        val jsonObject = obj::class.memberProperties.mapNotNull{ prop ->
            try {
                prop.isAccessible = true
                //val value = prop.get(obj as Nothing)
                val value = prop.call(obj)
                prop.name to (value?.let { infer(it) } ?: JsonNull)
            }catch (e: Exception){
                prop.name to JsonNull
            }
        }
        return JsonObject(jsonObject)
    }
}


