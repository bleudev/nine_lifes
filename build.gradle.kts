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

private val changelog = project.file("CHANGELOG.md").readText()
private val readme = project.file("README.md").readText()

private fun prConfigure(projectAndMinecraftVersions: Pair<String, String>, maxExclusiveVersion: String) {
    val versionSuffix = if (project.findProperty("beta_mode") == "true") "_beta" else ""
    project(":${projectAndMinecraftVersions.first}") {
        extensions.extraProperties.apply {
            set("minecraft_version", projectAndMinecraftVersions.second)
            set("max_exc_version", maxExclusiveVersion)
            set("mod_version", "${project.findProperty("general_version")}$versionSuffix+${projectAndMinecraftVersions.first}")
            set("changelog", changelog)
            set("readme", readme)
        }
    }
}
private fun prConfigure(v: String, maxExv: String) = prConfigure(v to v, maxExv)

private fun String.snapshot(num: Int): Pair<String, String> = this to "$this-snapshot-$num"
private fun String.pre(num: Int): Pair<String, String> = this to "$this-pre-$num"
private fun String.rc(num: Int): Pair<String, String> = this to "$this-rc-$num"

prConfigure("1.21.11", "26.1")
prConfigure("26.1".rc(1), "26.2")