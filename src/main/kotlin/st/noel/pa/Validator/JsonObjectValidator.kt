package org.example.st.noel.pa.Validator

import org.example.st.noel.pa.Interfaces.JsonValue
import org.example.st.noel.pa.Interfaces.JsonVisitor
import org.example.st.noel.pa.Models.*

/**
 * Validador de chaves duplicadas em objetos JSON.
 *
 * Implementa o padrão Visitor para percorrer a estrutura JSON e identificar se algum [JsonObject]
 * contém chaves repetidas. Utiliza um conjunto para rastrear as chaves já encontradas durante a validação.
 */
object JsonObjectValidator: JsonVisitor {
    /** Lista de mensagens de erro de validação. */
    private val validationErrors = mutableListOf<String>()
    /**
     * Conjunto usado para armazenar as chaves já encontradas durante a validação.
     * A duplicação de chaves no mesmo nível do JSON é considerada inválida.
     */
    private val keySet = mutableSetOf<String>()

    /**
     * Executa a validação de chaves duplicadas em um valor JSON.
     *
     * @param jsonValue O valor JSON (pode ser objeto, array, entre outros tipos.) a ser validado.
     * @return Uma lista de mensagens de erro, caso haja duplicações de chaves.
     */
    fun validateObject(jsonValue: JsonValue): List<String>{
        validationErrors.clear()
        //keySet.clear()
        jsonValue.accept(this)
        return validationErrors.toList()
    }
    /**
     * Visita um [JsonObject], verificando se há chaves duplicadas no conjunto atual.
     * Caso uma chave já exista no conjunto, um erro é registrado.
     *
     * @param obj O objeto JSON a ser validado.
     */
    override fun visit(obj: JsonObject){
        //validadeDuplicateKeys(obj)
        obj.properties.forEach{
            (chave, value) ->
            if (!keySet.add(chave)){
                validationErrors.add("Duplicação da Chave $chave no JsonObject informado!")
            }
            value.accept(this)
        }

    }
    /**
     * Visita um [JsonArray] e aplica a validação a cada elemento.
     *
     * @param arr O array JSON a ser validado.
     */
    override fun visit(arr: JsonArray) {
        arr.forEach {
            it.accept(this)
        }
    }
    /** Visita um [JsonString]. Não realiza nenhuma validação. */
    override fun visit(str: JsonString) {}
    /** Visita um [JsonNumber]. Não realiza nenhuma validação. */
    override fun visit(num: JsonNumber) {}
    /** Visita um [JsonBoolean]. Não realiza nenhuma validação. */
    override fun visit(bool: JsonBoolean) {}
    /** Visita um [JsonNull]. Não realiza nenhuma validação. */
    override fun visit(nul: JsonNull) {}
    /** Visita propriedade do objecto. Não realiza nenhuma validação. */
    override fun visitProperty(str: String, value: JsonValue) {}
}