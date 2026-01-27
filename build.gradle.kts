plugins {
    kotlin("jvm") version "2.3.0"
    id("com.gradleup.shadow") version "9.3.1"
}

group = "org.kanelucky"
version = "0.1.0"


dependencies {
    testImplementation(kotlin("test"))

    implementation("org.slf4j:slf4j-api:2.0.17")
    runtimeOnly("ch.qos.logback:logback-classic:1.5.24")

    implementation("net.minecrell:terminalconsoleappender:1.3.0")

    implementation("net.minestom:minestom:2026.01.08-1.21.11")
    implementation("io.github.togar2:MinestomPvP:2025.12.29-1.21.11")

    compileOnly("net.kyori:adventure-api:4.26.1")

    implementation("dev.hollowcube:schem:2.0.1")

    implementation("dev.rollczi:litecommands-minestom:3.10.9")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

kotlin {
    jvmToolchain(25)
}


tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "org.kanelucky.Minestom4fun"
        }
    }

    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        mergeServiceFiles()
        archiveClassifier.set("")
    }
}

tasks.test {
    useJUnitPlatform()
}