// dotenv test
plugins {
    id("com.sinch.gradle.dotenv")
}

gradle.extra["varName"] = "variable_from_dotenv"

val variable_from_dotenv: String by settings
val variable_from_properties: String by settings

try {
    println("dotenv variable is '$variable_from_dotenv' - accessed via delegate") // expected exception as non-existent
} catch (ex: Exception) {
    println(ex)
}
println("prop variable is '$variable_from_properties' - accessed via delegate") // expected valid value

println("dotenv variable is '${dotenv["variable_from_dotenv"]}' - accessed via dotenv[]") // expected valid value
println("prop variable is '${dotenv["variable_from_properties"]}' - accessed via dotenv[]") // expected null
