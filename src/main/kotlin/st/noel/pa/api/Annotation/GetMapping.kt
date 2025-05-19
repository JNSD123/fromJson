package org.example.st.noel.pa.api.Annotation

/**
 * Annotation para mapear métodos que respondem a requisições HTTP GET no endpoint.
 *
 * Pode ser aplicada a funções para definir a rota (caminho) do endpoint.
 *
 * @property path Caminho da rota HTTP GET. Se vazio, assume a raiz do controlador.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class GetMapping(val path: String = "")
