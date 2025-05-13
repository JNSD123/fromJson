package org.example.st.noel.pa.Models

import org.example.st.noel.pa.Interfaces.JsonValue
import org.example.st.noel.pa.Interfaces.JsonVisitor

data class JsonString(val value: String): JsonValue {
    override fun accept(visitor: JsonVisitor) {
        visitor.visit(this)
    }

    override fun toJsonString(): String {
        return "\"${escapeString(value)}\""
    }
    private fun escapeString(s: String):String{
        return s.replace("\\","\\\\")
            .replace("\b","\\b")
            .replace("\n","\\n")
            .replace("\r","\\r")
            .replace("\t","\\t")
    }
}