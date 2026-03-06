plugins {
    kotlin("jvm") version "2.3.0"
}

group = "org.kanelucky"
description = "minestom4fun-config"
version = "0.1.3"

dependencies {
    implementation(project(":minestom4fun-api:java"))
    implementation("net.minestom:minestom:2026.02.09-1.21.11")
    implementation("eu.okaeri:okaeri-configs-yaml-snakeyaml:6.1.0-beta.1")
    implementation("eu.okaeri:okaeri-configs-serdes-commons:6.1.0-beta.1")
    implementation("it.unimi.dsi:fastutil:8.5.15")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}