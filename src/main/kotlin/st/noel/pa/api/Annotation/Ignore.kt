package org.example.st.noel.pa.api.Annotation

/**
 * Annotation para indicar que uma propriedade deve ser ignorada em processos
 * como serialização, persistência ou mapeamento de dados.
 *
 * Pode ser usada para propriedades que não devem ser incluídas no JSON ou banco de dados.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Ignore
