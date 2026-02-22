import org.gradle.kotlin.dsl.support.serviceOf
import java.io.ByteArrayOutputStream

plugins {
    kotlin("jvm") version "2.3.0"
    kotlin("plugin.serialization") version "2.3.0"
    id("com.gradleup.shadow") version "9.3.1"
}

group = "org.kanelucky"
version = "0.1.3"

fun getShortGitHash(): String {
    val execOperations = project.serviceOf<ExecOperations>()
    val stdout = ByteArrayOutputStream()

    execOperations.exec {
        commandLine("git", "rev-parse", "--short", "HEAD")
        standardOutput = stdout
        isIgnoreExitValue = true
    }

    return stdout.toString().trim().ifEmpty { "nogit" }
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("ch.qos.logback:logback-classic:1.5.24")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.snakeyaml:snakeyaml-engine:2.7")
    implementation("com.formdev:flatlaf:3.4")

    implementation("net.minestom:minestom:2026.02.09-1.21.11")
    implementation("io.github.togar2:MinestomPvP:2025.12.29-1.21.11")
    implementation("dev.hollowcube:polar:1.15.0")
    implementation("dev.hollowcube:schem:2.0.1")
    implementation("dev.rollczi:litecommands-minestom:3.10.9")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.3")
    implementation("com.charleskorn.kaml:kaml:0.61.0")
    implementation("com.github.Kanelucky:Minestom-Dashboard:v0.1.1")
    implementation(files("libs/worldgen-0.1.0.jar"))
    implementation(files("libs/MinestomFluids.jar"))

    compileOnly("net.kyori:adventure-api:4.26.1")
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
            attributes(
                "Main-Class" to "org.kanelucky.server.Minestom4fun",
                "Implementation-Title" to "Minestom4fun",
                "Implementation-Version" to project.version,
                "Git-Commit" to getShortGitHash()
            )
        }
    }

    shadowJar {
        mergeServiceFiles()
        archiveFileName.set(
            "minestom4fun-server-${project.version}-${getShortGitHash()}-shaded.jar"
        )
    }

    build {
        dependsOn(shadowJar)
    }

    test {
        useJUnitPlatform()
    }
}

tasks.register<JavaExec>("runServer") {
    group = "application"
    description = "Run Minecraft Server"

    mainClass.set("org.kanelucky.server.Minestom4fun")
    classpath = sourceSets["main"].runtimeClasspath

    jvmArgs("-Xms1G", "-Xmx2G")
    workingDir = file("run")
}
