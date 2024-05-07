plugins {
    kotlin("jvm") version "1.9.22"
}

group = "org.itonemeetup"
version = "1.0-SNAPSHOT"

/* Версии зависимостей */
val jvmToolChain = 17
val log4jApiKotlinVersion = "1.4.0"
val log4jVersion = "2.23.1"
val allureVersion = "2.26.0"
val aspectJVersion = "1.9.22"
val restAssuredVersion = "5.4.0"
val jacksonVersion = "2.16.1"

repositories {
    mavenCentral()
}

/* Настройка агента для AspectJ: https://allurereport.org/docs/junit5/#configure-aspectj  */
val agent: Configuration by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = true
}

dependencies {
    /* ----- Логирование */
    /* Log4j Kotlin API: https://logging.apache.org/log4j/kotlin/latest/ */
    implementation("org.apache.logging.log4j:log4j-api-kotlin:$log4jApiKotlinVersion")
    /* Импорт BOM: https://docs.gradle.org/current/userguide/platforms.html#sub:bom_import */
    implementation(platform("org.apache.logging.log4j:log4j-bom:$log4jVersion"))
    /* При импорте BOM версию артефакта можно не указывать */
    implementation("org.apache.logging.log4j:log4j-slf4j-impl")

    /* ----- Отчет о результатах выполнения тестов */
    /* Документация allure: https://allurereport.org/docs/junit5/ */
    testImplementation(platform("io.qameta.allure:allure-bom:$allureVersion"))
    testImplementation("io.qameta.allure:allure-junit5")
    agent("org.aspectj:aspectjweaver:$aspectJVersion")

    /* -----Адаптеры (драйверы) */
    /* Адаптер для работы с REST-API */
    testImplementation("io.rest-assured:rest-assured:$restAssuredVersion")
    testImplementation("io.rest-assured:kotlin-extensions:$restAssuredVersion")
    /* Адаптер для работы с JSON */
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
    jvmArgs = listOf(
        "-javaagent:${agent.singleFile}"
    )
}

kotlin {
    jvmToolchain(jvmToolChain)
}
