rootProject.name = "Minestom4fun"
include(":minestom4fun-server")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        flatDir { dir ("libs") }
        maven { url = uri("https://reposilite.atlasengine.ca/public") }
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://repo.panda-lang.org/releases") }
    }
}