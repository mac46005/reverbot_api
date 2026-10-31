
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.example"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}
dependencies {
    implementation("com.preciado:preciadoapiutilities:1.1.0")
    implementation("com.zaxxer:HikariCP:6.2.1")
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.config.yaml)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.di)
    implementation(ktorLibs.server.netty)
    implementation(libs.h2database.h2)
    implementation(libs.logback.classic)
    implementation(libs.postgresql)
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")

    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
