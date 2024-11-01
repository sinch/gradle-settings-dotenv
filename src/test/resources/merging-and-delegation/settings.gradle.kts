// dotenv test
plugins {
    id("com.sinch.gradle.dotenv")
}

gradle.extra["varName"] = "variable_from_dotenv"

val variable_from_dotenv: String by settings
val variable_from_properties: String by settings

println("dotenv variable is '$variable_from_dotenv' - accessed via delegate") // expected valid value
println("prop variable is '$variable_from_properties' - accessed via delegate") // expected valid value

println("dotenv variable is '${dotenv["variable_from_dotenv"]}' - accessed via dotenv[]") // expected valid value
println("prop variable is '${dotenv["variable_from_properties"]}' - accessed via dotenv[]") // expected null

val firstRealEnvVar =
    System
        .getenv()
        .entries
        .stream()
        .toList()[0]
println("first real env var '${firstRealEnvVar.key}' is '${firstRealEnvVar.value}'")

val ext = settings.extensions.extraProperties
println("first real env var via prop '${firstRealEnvVar.key}' is '${ext[firstRealEnvVar.key]}'")
