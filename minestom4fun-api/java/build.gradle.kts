plugins {
    kotlin("jvm") version "2.3.0"
}

group = "org.kanelucky"
description = "minestom4fun-api-java"
version = "0.1.3"

dependencies {
    implementation("net.minestom:minestom:2026.02.09-1.21.11")
    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}