import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.string.shouldContain
import org.gradle.testkit.runner.GradleRunner
import java.io.File

class IntegrationTests : StringSpec() {
    val projectDirRoot = "src/test/resources"

    fun getGradleRunnerOutput(projectDir: String): String {
        val output =
            GradleRunner
                .create()
                .withProjectDir(File("$projectDirRoot/$projectDir"))
                .withPluginClasspath()
                .withArguments("properties")
                .build()
                .output
        println(output)
        return output
    }

    init {
        "default dotenv location" {
            getGradleRunnerOutput("default-dotenv-location") shouldContain "VARIABLE is 'baz9001'"
        }

        "custom dotenv location" {
            getGradleRunnerOutput("custom-dotenv-location") shouldContain "TEST_VARIABLE is 'foobar2137'"
        }

        "merging and delegation" {
            val output = getGradleRunnerOutput("merging-and-delegation")
            listOf(
                "dotenv variable is 'dotenvvar' - accessed via delegate",
                "prop variable is 'propvar' - accessed via delegate",
                "dotenv variable is 'dotenvvar' - accessed via dotenv[]",
                "prop variable is 'null' - accessed via dotenv[]",
            ).forEach { output shouldContain it }
        }

        "merging disabled" {
            val output = getGradleRunnerOutput("merging-disabled")
            listOf(
                "Cannot get non-null property 'variable_from_dotenv' on settings 'merging-disabled' as it does not exist",
                "Cannot get non-null property 'variable_from_dotenv' on root project 'merging-disabled' as it does not exist",
                "prop variable is 'propvar' - accessed via delegate",
                "dotenv variable is 'dotenvvar' - accessed via dotenv[]",
                "prop variable is 'null' - accessed via dotenv[]",
            ).forEach { output shouldContain it }
        }
    }
}
