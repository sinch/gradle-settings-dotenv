//
val varName: String by gradle.extra
println(varName)
println(dotenv[varName])

try {
    val variable_from_dotenv: String by project
    println("var value (via project delegate): $variable_from_dotenv")
} catch (ex: Exception) {
    println(ex)
}
