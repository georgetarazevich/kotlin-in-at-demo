plugins {
    kotlin("jvm") version "1.9.22"
}

group = "org.itonemeetup"
version = "1.0-SNAPSHOT"

val jvmToolChain = 17

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(jvmToolChain)
}
