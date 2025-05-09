package org.example.st.noel.pa.api.Annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class GetMapping(val path: String = "")
