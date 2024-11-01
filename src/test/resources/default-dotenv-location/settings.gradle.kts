// dotenv test
plugins {
    id("com.sinch.gradle.dotenv")
}

val varName = "VARIABLE"
gradle.extra["varName"] = varName

println("$varName is '${dotenv[varName]}'")

val VARIABLE: String by dotenv
println(VARIABLE)
