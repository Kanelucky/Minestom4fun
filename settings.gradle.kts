rootProject.name = "Minestom4fun"
include(
    ":minestom4fun-server",
    "minestom4fun-world",
    "minestom4fun-entity",
    "minestom4fun-api",
    ":minestom4fun-api:java",
    ":minestom4fun-api:python",
    "minestom4fun-fluid",
    "minestom4fun-config"
)
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        flatDir { dir("libs") }
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://repo.panda-lang.org/releases") }
        maven { url = uri("https://mvn.everbuild.org/public") }
        maven { url = uri("https://repo.codemc.io/repository/maven-public/") }
        maven { url = uri("https://repo.okaeri.cloud/releases") }
    }
}
