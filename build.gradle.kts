group = "org.example"
version = "1.0-SNAPSHOT"
val ktor_version = "3.1.3"

plugins {
    kotlin("jvm") version "2.1.10"
    application
}

application{
    mainClass.set("st.noel.pa.MainKt")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // Dependências para testes
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test:2.1.10")  // Para usar assertivas do Kotlin
    //Configuração do Ktor Server Kotlin
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging:$ktor_version")
    implementation("ch.qos.logback:logback-classic:1.4.11") // For logging

}

tasks.test {
    useJUnitPlatform()
}

