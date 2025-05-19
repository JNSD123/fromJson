package org.example.st.noel.pa.Models

import org.example.st.noel.pa.Interfaces.JsonValue
import org.example.st.noel.pa.Interfaces.JsonVisitor
/**
 * Representa um valor numérico em uma estrutura JSON.
 *
 * Suporta qualquer tipo que herde de [Number], como `Int`, `Double`, `Float`, etc.
 * Implementa a interface [JsonValue] para serialização e visitação.
 *
 * @property value Valor numérico armazenado.
 */

data class JsonNumber(val value: Number) : JsonValue {
    /**
     * Aplica o padrão Visitor ao valor numérico.
     *
     * @param visitor O visitante que será aplicado.
     */
    override fun accept(visitor: JsonVisitor) {
        visitor.visit(this);
    }
    /**
     * Converte o valor numérico para sua representação textual em JSON.
     *
     * @return String contendo o número (sem aspas).
     */
    override fun toJsonString(): String {
        return value.toString();
    }
}
