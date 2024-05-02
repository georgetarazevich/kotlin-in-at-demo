plugins {
    kotlin("jvm") version "1.9.22"
}

group = "org.itonemeetup"
version = "1.0-SNAPSHOT"

/* Версии зависимостей */
val jvmToolChain = 17
val log4jApiKotlinVersion = "1.4.0"
val log4jVersion = "2.23.1"

repositories {
    mavenCentral()
}

dependencies {
    /* Log4j Kotlin API: https://logging.apache.org/log4j/kotlin/latest/ */
    implementation("org.apache.logging.log4j:log4j-api-kotlin:$log4jApiKotlinVersion")
    /* Импорт BOM: https://docs.gradle.org/current/userguide/platforms.html#sub:bom_import */
    implementation(platform("org.apache.logging.log4j:log4j-bom:$log4jVersion"))
    /* При импорте BOM версию артефакта можно не указывать */
    implementation("org.apache.logging.log4j:log4j-core")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(jvmToolChain)
}
