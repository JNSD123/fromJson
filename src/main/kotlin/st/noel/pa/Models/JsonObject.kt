package org.example.st.noel.pa.Models

import org.example.st.noel.pa.Interfaces.JsonValue
import org.example.st.noel.pa.Interfaces.JsonVisitor

/**
 * Representa um JsonObject, armazenando pares de chave-valor.
 *
 * Cada chave é uma [String] e o valor é uma instância de [JsonValue].
 * Permite serialização para string JsonString, aplicação do padrão Visitor,
 * e operações como filtragem, adição e remoção de propriedades.
 *
 * @property properties Lista de pares chave-valor que compõem o JsonObeject.
 */
data class JsonObject(
    //public val properties: MutableMap<String, JsonValue> = mutableMapOf()
    val properties: List<Pair<String, JsonValue>>
): JsonValue {
    /**
     * Construtor alternativo que permite criar o JsonObject a partir de um [Map].
     *
     * @param map Map com chaves String e valores [JsonValue].
     */
    constructor(map: Map<String, JsonValue>): this(map.entries.map {
        it.toPair()
    })
    /**
     * Aplica o padrão Visitor a JsonObject e todas as suas propriedades.
     *
     * @param visitor O visitante a ser aplicado.
     */
    override fun accept(visitor: JsonVisitor) {
        /*
        * Chama o método visit(obj: JsonObject) do JsonVisitor
        */
        visitor.visit(this)
        val visitedKeys = mutableListOf<String>()
        properties.forEach{ (key,value) ->
            visitor.visitProperty(key,value)
            value.accept(visitor)
        }
    }
    /**
     * Converte o JsonObject em uma string do JSON válida.
     *
     * @return um JSON como [String].
     */
    override fun toJsonString(): String {
        return  properties.joinToString(
            separator = ", ",
            prefix = "{",
            postfix = "}"
        ){ (chave, value)->
            "\"$chave\": ${value.toJsonString()}"
        }
    }
    /**
     * Acessa a primeira ocorrência de uma propriedade pelo nome da chave.
     *
     * @param key A chave da propriedade desejada.
     * @return O valor associado à chave ou null se não for encontrada.
     */
    operator fun get(key: String): JsonValue? =
        properties.firstOrNull(){ it.first == key}?.second
    /**
     * Obtém todas as ocorrências de uma determinada chave.
     *
     * @param key A chave a ser buscada.
     * @return Lista de valores associados a chave.
     */
    fun getAll(key:String): List<JsonValue> =
        properties.filter { it.first == key }.map { it.second }
    //Adiciona propriedade ao objecto JSON
    /**
     * Retorna uma nova instância de [JsonObject] com uma nova propriedade adicionada.
     *
     * @param key Nome da chave.
     * @param value Valor JSON a ser associado.
     * @return Novo objeto com a propriedade adicionada.
     */
    fun addProperty(key: String, value: JsonValue): JsonObject =
        JsonObject(properties + (key to value))
    /**
     * Filtra as propriedades do objeto com base em um paramêtro.
     *
     * @param predicate Função que recebe a chave e o valor e retorna um booleano.
     * @return Novo [JsonObject] contendo apenas os pares que satisfazem o predicado.
     */
    fun filterObject(predicate: (String, JsonValue)->Boolean): JsonObject =
        JsonObject(properties.filter { (k, v)->predicate(k, v) })
    /**
     * Converte o objeto em um [Map], ignorando chaves duplicadas.
     *
     * @return Um mapa com chaves únicas.
     */
    fun toMap(): Map<String, JsonValue> = properties.toMap()
    /**
     * Remove chaves duplicadas, mantendo apenas a primeira ocorrência de cada uma.
     *
     * @return Novo [JsonObject] com chaves únicas.
     */
    fun removeDuplicateKeys():JsonObject{
        val seenKeys = mutableListOf<String>()
        val filteredProps = properties.filter { (key,_)->
            if (key in seenKeys){
                false
            }else{
                seenKeys.add(key)
                true
            }
        }
        return JsonObject(filteredProps)
    }
}