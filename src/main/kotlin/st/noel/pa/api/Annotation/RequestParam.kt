package org.example.st.noel.pa.api.Annotation

/**
 * Annotation para marcar parâmetros da função que serão preenchidos com valores de parâmetros
 * de consulta (query parameters) da URL em requisições HTTP.
 *
 * @property name Nome do parâmetro de consulta esperado na URL. Se vazio, assume o nome do parâmetro.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequestParam(val name: String = "")
