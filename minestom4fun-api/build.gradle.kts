plugins {
    kotlin("jvm") version "2.3.0"
}

group = "org.kanelucky"
description = "minestom4fun-api"
version = "0.1.3"

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}