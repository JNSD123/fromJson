package org.example.st.noel.pa.api.Annotation

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Column(val name: String = "", val primaryKey: Boolean = false)
