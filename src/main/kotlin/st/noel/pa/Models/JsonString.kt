package org.example.st.noel.pa.Models

import org.example.st.noel.pa.Interfaces.JsonValue
import org.example.st.noel.pa.Interfaces.JsonVisitor

/**
 * Representa um valor do tipo string em um JsonString.
 *
 * Implementa a interface [JsonValue] e fornece métodos para
 * serialização e visitação via [JsonVisitor].
 *
 * @property value O valor da string representada.
 */
data class JsonString(val value: String): JsonValue {
    /**
     * Aplica o padrão Visitor ao valor desta string.
     *
     * @param visitor O visitante que será aplicado.
     */
    override fun accept(visitor: JsonVisitor) {
        visitor.visit(this)
    }
    /**
     * Retorna a representação do valor como string JsonString, com aspas.
     *
     * @return A string formatada como JSON.
     */
    override fun toJsonString(): String {
        return "\"${escapeString(value)}\""
    }
    /**
     * Escapa caracteres especiais de uma string para formato JSON.
     *
     * @param s A string a ser escapada.
     * @return A string com os caracteres especiais escapados.
     */
    private fun escapeString(s: String):String{
        return s.replace("\\","\\\\")
            .replace("\b","\\b")
            .replace("\n","\\n")
            .replace("\r","\\r")
            .replace("\t","\\t")
    }
}