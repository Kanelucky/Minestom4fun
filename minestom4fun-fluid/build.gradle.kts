plugins {
    kotlin("jvm") version "2.3.0"
}

group = "org.kanelucky"
description = "minestom4fun-fluid"
version = "0.1.3"

dependencies {
    implementation("net.minestom:minestom:2026.02.09-1.21.11")
    implementation(project(":minestom4fun-api:java"))
    implementation(project(":minestom4fun-config"))
    implementation("it.unimi.dsi:fastutil:8.5.15")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}