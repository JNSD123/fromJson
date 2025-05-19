package org.example.st.noel.pa.Models

import org.example.st.noel.pa.Interfaces.JsonValue
import org.example.st.noel.pa.Interfaces.JsonVisitor

/**
 * Representa um valor nulo em uma estrutura JSON.
 *
 * Esta implementação é um singleton (objeto) que indica o valor `null` conforme o padrão JSON.
 * Implementa a interface [JsonValue] para suportar serialização e aplicação do padrão Visitor.
 */
object JsonNull : JsonValue {
    /**
     * Aplica o padrão Visitor ao valor `null`.
     *
     * @param visitor O visitante que será aplicado ao valor nulo.
     */
    override fun accept(visitor: JsonVisitor) {
        visitor.visit(this);
    }
    /**
     * Converte o valor nulo para sua representação textual em JSON.
     *
     * @return String `"null"`, conforme o padrão JSON.
     */
    override fun toJsonString(): String {
        return "null";
    }
}