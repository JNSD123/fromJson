package org.example.st.noel.pa.Interfaces

/**
 * Interface base para todos os tipos de valores JSON da biblioteca.
 *
 * Cada implementação representa um tipo concreto de valor JSON, como objetos,
 * arrays, strings, números (int, double), booleanos ou nulos. Essa interface define o
 * comportamento comum necessário para a serialização e para o padrão Visitor.
 */
interface JsonValue {
    /**
     * Aceita um visitante conforme o padrão de projeto Visitor.
     *
     * @param visitor A instância de [JsonVisitor] que irá visitar este valor JSON.
     */
    abstract fun accept(visitor: JsonVisitor)
    /**
     * Converte o valor JSON para a representação de texto em JSON.
     *
     * @return uma string contendo a representação JSON válida.
     */
    abstract fun toJsonString(): String
}