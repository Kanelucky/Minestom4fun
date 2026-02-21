plugins {
    kotlin("jvm") version "2.3.0"
    id("com.gradleup.shadow") version "9.3.1"
}

group = "org.kanelucky"
version = "0.1.3"
description = "A Minecraft: Java Edition server software built on top of Minestom, " +
        "designed to do what Minestom was never meant for: Survival"

tasks.named<Jar>("jar") {
    enabled = false
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    enabled = false
}

tasks.test {
    useJUnitPlatform()
}