package org.example.st.noel.pa.api.Annotation

/**
 * Annotation para marcar classes como controlller para requisições HTTP.
 *
 * @property path Caminho base (prefixo) para todas as rotas definidas dentro do controlador.
 * Se vazio, assume a raiz ("/").
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RestController(val path: String = "")
