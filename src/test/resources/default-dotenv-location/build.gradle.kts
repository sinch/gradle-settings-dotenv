//
val varName: String by gradle.extra
println(varName)
println(dotenv[varName])

val VARIABLE: String by dotenv
println(VARIABLE)
