plugins {
    kotlin("jvm") version "2.3.10"
    kotlin("plugin.serialization") version "2.3.10"
    id("fabric-loom") version "1.15.4" apply false
    id("com.modrinth.minotaur") version "2.+" apply false
    id("maven-publish")
}

repositories {
    mavenCentral()
}

val changelog = project.file("CHANGELOG.md").readText()
val readme = project.file("README.md").readText()
fun configure(v: String, maxExv: String, snapshot: Int? = null) {
    val versionSuffix = if (project.findProperty("beta_mode") == "true") "_beta" else ""
    project(":$v") {
        extensions.extraProperties.apply {
            set("minecraft_version", "$v${if (snapshot == null) "" else "-snapshot-$snapshot"}")
            set("max_exc_version", maxExv)
            set("mod_version", "${project.findProperty("general_version")}$versionSuffix+$v")
            set("changelog", changelog)
            set("readme", readme)
        }
    }
}
configure("1.21.10", "1.21.11")
configure("1.21.11", "1.21.12")
configure("26.1", "26.2", 9)