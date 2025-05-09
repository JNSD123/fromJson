package org.example.st.noel.pa.Inference

import org.example.st.noel.pa.Interfaces.JsonValue
import org.example.st.noel.pa.Models.*
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

object JsonInferrer_ {
    fun infer(obj: Any?): JsonValue{
        return when(obj){
            null -> JsonNull
            is Boolean -> JsonBoolean(obj)
            is Number -> JsonNumber(obj)
            is String -> JsonString(obj)
            is Enum<*> -> JsonString(obj.name)
            is List<*> -> inferArray(obj.map { infer(it) }.toMutableList())
            is Map<*, *> -> inferMap(obj.mapValues { infer(it.value) }.toMutableMap())
            else -> inferDataClass(obj)
        }
    }
    private fun inferArray(list: List<*>): JsonArray{
        val jsonArray = JsonArray()
        list.forEach{
                item -> jsonArray.addElmentArray(infer(item))
        }
        return jsonArray
    }
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
    /*private fun inferMap(map: Map<*,*>): JsonObject{
        val jsonObject = JsonObject()
        map.entries.forEach {
                (key, value) ->
            if (key !is String){
                throw IllegalArgumentException("A chave do mapeamento deve ser String")
            }
            jsonObject[key] = infer(value)
        }
        return jsonObject
    }*/
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
    /*private fun inferDataClass(obj: Any): JsonObject{
        val jsonObject = JsonObject()
        obj::class.members.forEach{ prop ->
            try {
                prop.isAccessible = true
                //val value = prop.get(obj as Nothing)
                val value = prop.call(obj)
                jsonObject[prop.name] = if (value != null) infer(value) else JsonNull
            }catch (e: Exception){
                jsonObject[prop.name] = JsonNull
            }
        }
        return jsonObject
    }*/
}


