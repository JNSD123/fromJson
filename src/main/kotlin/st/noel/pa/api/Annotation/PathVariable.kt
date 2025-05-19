package org.example.st.noel.pa.api.Annotation


/**
 * Annotation para marcar parâmetros da função que será preenchidos com valores extraídos da URL,
 * correspondentes a variáveis de caminho (path variables) em rotas ou controller.
 *
 * @property name Nome da variável no caminho da URL. Se vazio, assume o nome do parâmetro.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class PathVariable(val name: String = "")
