package org.example.st.noel.pa.Models

import org.example.st.noel.pa.Interfaces.JsonValue
import org.example.st.noel.pa.Interfaces.JsonVisitor

/**
 * Representa um valor booleano em uma estrutura JSON.
 *
 * Implementa a interface [JsonValue] e permite serialização e aplicação do padrão Visitor.
 *
 * @property value Valor booleano (true ou false) representado por este objeto.
 */
data class JsonBoolean(val value: Boolean): JsonValue{
    /**
     * Aplica o padrão Visitor ao valor booleano.
     *
     * @param visitor O visitante que será aplicado.
     */
    override fun accept(visitor: JsonVisitor) {
        visitor.visit(this)
    }
    /**
     * Converte o valor booleano para sua representação textual em JSON.
     *
     * @return "true" ou "false".
     */
    override fun toJsonString(): String {
        return value.toString()
    }
}
