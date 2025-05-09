package org.example.st.noel.pa.api.Annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RestController(val path: String = "")
