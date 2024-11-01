// dotenv test
plugins {
    id("com.sinch.gradle.dotenv")
}

val varName = "TEST_VARIABLE"
gradle.extra["varName"] = varName

println("$varName is '${dotenv[varName]}'")
