plugins {
    kotlin("jvm") version "2.3.0"
    kotlin("plugin.serialization") version "2.3.0"
}

group = "org.kanelucky"
description = "minestom4fun-world"
version = "0.1.3"

kotlin {
    jvmToolchain(25)
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.snakeyaml:snakeyaml-engine:2.7")
    implementation("com.formdev:flatlaf:3.4")

    implementation("net.minestom:minestom:2026.02.09-1.21.11")
    implementation("dev.hollowcube:polar:1.15.0")
    implementation("dev.hollowcube:schem:2.0.1")
    implementation("com.charleskorn.kaml:kaml:0.61.0")
    implementation("de.articdive:jnoise-pipeline:4.1.0")

    implementation(project(":minestom4fun-api:java"))
    implementation(project(":minestom4fun-config"))
    implementation("eu.okaeri:okaeri-configs-yaml-snakeyaml:6.1.0-beta.1")
    implementation("eu.okaeri:okaeri-configs-serdes-commons:6.1.0-beta.1")

    compileOnly("net.kyori:adventure-api:4.26.1")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}