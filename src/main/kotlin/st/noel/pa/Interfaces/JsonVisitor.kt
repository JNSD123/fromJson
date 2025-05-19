package org.example.st.noel.pa.Interfaces

import org.example.st.noel.pa.Models.*
/*
* Interface que define o comportamento do padrão do Visitor
* para os tipos de valores JSON da biblioteca.
*
* Permite realizar operações em diferentes tipos de [JsonValue] sem
* modificar suas classes diretamente.
*/
interface JsonVisitor {
    /**
     * Visita o objeto JSON ([JsonObject]).
     *
     * @param obj O objeto JSON a ser visitado.
     */
    fun visit(obj: JsonObject)
    /**
     * Visita o array JSON ([JsonArray]).
     *
     * @param arr O array JSON a ser visitado.
     */
    fun visit(arr: JsonArray)
    /**
     * Visita uma string JSON ([JsonString]).
     *
     * @param str A string JSON a ser visitada.
     */
    fun visit(str: JsonString)
    /**
     * Visita um número(int,double) JSON ([JsonNumber]).
     *
     * @param numb O número JSON a ser visitado.
     */
    fun visit(numb: JsonNumber)
    /**
     * Visita um valor booleano JSON ([JsonBoolean]).
     *
     * @param bool O valor booleano JSON a ser visitado.
     */
    fun visit(bool: JsonBoolean)
    /**
     * Visita um valor nulo JSON ([JsonNull]).
     *
     * @param nul O valor nulo JSON a ser visitado.
     */
    fun visit(nul: JsonNull)
    /**
     * Visita a propriedade de um objeto JSON.
     *
     * @param str O nome da propriedade.
     * @param value O valor associado à propriedade.
     */
    fun visitProperty(str: String, value: JsonValue)
}