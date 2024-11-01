import com.sinch.gradle.dotenv.DotenvPlugin
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class UnitTests : StringSpec() {
    init {
        "simple string is parsed" {
            DotenvPlugin.parseDotenv(listOf("key=value")) shouldBe mapOf("key" to "value")
        }

        "multiple strings are parsed" {
            DotenvPlugin.parseDotenv(
                listOf(
                    "key=value",
                    "key2=value2",
                ),
            ) shouldBe
                mapOf(
                    "key" to "value",
                    "key2" to "value2",
                )
        }

        "blank and #comment lines are ignored" {
            DotenvPlugin.parseDotenv(listOf("key=value", "      ", "#comment")) shouldBe mapOf("key" to "value")
        }

        "no key throws" {
            val exception = shouldThrow<IllegalArgumentException> { DotenvPlugin.parseDotenv(listOf("=value")) }
            exception.message shouldContain "strings need to have non-blank key"
        }

        "blank key throws" {
            val exception = shouldThrow<IllegalArgumentException> { DotenvPlugin.parseDotenv(listOf("   =value")) }
            exception.message shouldContain "strings need to have non-blank key"
        }

        "no value is allowed" {
            DotenvPlugin.parseDotenv(listOf("key=")) shouldBe mapOf("key" to "")
        }

        "blank value is allowed" {
            DotenvPlugin.parseDotenv(listOf("key=   ")) shouldBe mapOf("key" to "   ")
        }

        "quotes are treated verbatim and preserved" {
            DotenvPlugin.parseDotenv(listOf("\"key\"=\"  asd \"")) shouldBe mapOf("\"key\"" to "\"  asd \"")
        }

        "leading/trailing whitespaces are treated verbatim and preserved" {
            DotenvPlugin.parseDotenv(listOf("  key  =  asd  ")) shouldBe mapOf("  key  " to "  asd  ")
        }

        "no assignment throws" {
            val exception = shouldThrow<IllegalArgumentException> { DotenvPlugin.parseDotenv(listOf("keyvalue")) }
            exception.message shouldContain "strings need to have two parts"
        }

        "neither key nor value throws" {
            val exception = shouldThrow<IllegalArgumentException> { DotenvPlugin.parseDotenv(listOf("=")) }
            exception.message shouldContain "strings need to have non-blank key"
        }

        "following assignments are treated as simple string" {
            DotenvPlugin.parseDotenv(listOf("a=b=c")) shouldBe mapOf("a" to "b=c")
        }
    }
}
