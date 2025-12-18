plugins {
    kotlin("jvm") version "2.2.21"
    id("fabric-loom") version "1.14-SNAPSHOT" apply false
    id("maven-publish")
}

repositories {
    mavenCentral()
}

fun configure(v: String, maxExv: String) {
    val versionSuffix = if (project.findProperty("beta_mode") == "true") "_beta" else ""
    project(":$v") {
        extensions.extraProperties.apply {
            set("minecraft_version", v)
            set("max_exc_version", maxExv)
            set("mod_version", "${project.findProperty("general_version")}$versionSuffix+$v")
        }
    }
}

configure("1.21.6", "1.21.9")
configure("1.21.9", "1.21.11")
configure("1.21.11", "1.21.12")