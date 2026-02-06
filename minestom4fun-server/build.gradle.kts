plugins {
    kotlin("jvm") version "2.3.0"
    id("com.gradleup.shadow") version "9.3.1"
    kotlin("plugin.serialization") version "2.3.0"
}

group = "org.kanelucky"
version = "0.1.2"


dependencies {
    testImplementation(kotlin("test"))

    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("ch.qos.logback:logback-classic:1.5.24")

    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    implementation("org.snakeyaml:snakeyaml-engine:2.7")

    implementation("net.minestom:minestom:2026.01.08-1.21.11")
    implementation("io.github.togar2:MinestomPvP:2025.12.29-1.21.11")
    implementation("ca.atlasengine:atlas-projectiles:2.1.5")
    implementation("dev.hollowcube:polar:1.15.0")
    implementation("dev.lu15:luckperms-minestom:8feae07")

    compileOnly("net.kyori:adventure-api:4.26.1")

    implementation("dev.hollowcube:schem:2.0.1")

    implementation("dev.rollczi:litecommands-minestom:3.10.9")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
    withJavadocJar()
}

kotlin {
    jvmToolchain(25)
}


tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "org.kanelucky.server.Minestom4fun"
            attributes["Implementation-Version"] = project.version
            attributes["Implementation-Title"] = "Minestom4fun"
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

tasks.register<JavaExec>("runServer") {
    group = "application"
    description = "Run Minecraft Server"

    mainClass.set("org.kanelucky.server.Minestom4fun")
    classpath = sourceSets["main"].runtimeClasspath

    jvmArgs(
        "-Xms1G",
        "-Xmx2G"
    )

    workingDir = file("run")
}

tasks.test {
    useJUnitPlatform()
}