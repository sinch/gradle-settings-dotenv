// dotenv plugin
plugins {
    kotlin("jvm") version libs.versions.kotlin

    alias(libs.plugins.plugin.publish)
}

assert(JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_17))

group = "com.sinch.gradle"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
}

val gitRepoUrl = "https://github.com/sinch/gradle-settings-dotenv"

@Suppress("UnstableApiUsage")
gradlePlugin {
    website = gitRepoUrl
    vcsUrl = "$gitRepoUrl.git"

    plugins {
        create("settingsDotenvPlugin") {
            id = "$group.${rootProject.name}"
            displayName = "Gradle Settings .env (dotenv) parser"
            description =
                "A plugin allowing easy access to variables defined in .env at both settings- and project-level."
            tags = listOf("env", "dotenv", "envfile", "environment", "settings")
            implementationClass = "$id.DotenvPlugin"
        }
    }
}
