//
val varName: String by gradle.extra
println("var name: $varName")
println("var value (via dotenv accessor): ${dotenv[varName]}")

val variable_from_properties: String by project
println("var value (via project delegate): $variable_from_properties")

val variable_from_dotenv: String by project
println("var value (via project delegate): $variable_from_dotenv")
