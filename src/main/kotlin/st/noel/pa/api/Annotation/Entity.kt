package org.example.st.noel.pa.api.Annotation

/**
 * Annotation para marcar uma classe como uma entidade que representa uma tabela no banco de dados.
 *
 * @property tableName Nome da tabela no banco de dados. Se vazio, assume o nome da classe.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Entity(val tableName: String = "")
