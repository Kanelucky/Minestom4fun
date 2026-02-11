plugins {
    kotlin("jvm") version "2.3.0"
    id("com.gradleup.shadow") version "9.3.1"
}

group = "org.kanelucky"
version = "0.1.3"

tasks.named<Jar>("jar") {
    enabled = false
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    enabled = false
}

tasks.test {
    useJUnitPlatform()
}