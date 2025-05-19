package org.example.st.noel.pa.api.Annotation


/**
 * Annotation para mapear propriedades de uma classe para colunas de uma entidade de banco de dados.
 *
 * Pode ser aplicada a propriedades (fields) de uma data class ou classe normal.
 *
 * @property name Nome da coluna no banco de dados. Se vazio, assume o nome da propriedade.
 * @property primaryKey Indica se a coluna é chave primária. Padrão é `false`.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Column(val name: String = "", val primaryKey: Boolean = false)
