package com.sinch.gradle.dotenv

import com.sinch.gradle.dotenv.DotenvPluginExtension.Companion.NAME
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.internal.extensions.core.extra

open class DotenvPluginExtension {
    companion object {
        const val NAME = "dotenv"
    }

    var vars: Map<String, String> = mapOf()
}

@Suppress("unused")
class DotenvPlugin : Plugin<Settings> {
    companion object {
        val logger: Logger = Logging.getLogger(DotenvPlugin::class.java)

        @Suppress("MemberVisibilityCanBePrivate")
        fun parseDotenv(lines: List<String>) =
            lines
                .filterNot { it.isBlank() || it.startsWith("#") }
                .associate {
                    val limit = 2
                    val parts = it.split("=", limit = limit)
                    if (parts.size < limit) {
                        throw IllegalArgumentException("input strings need to have two parts separated by '=', instead got\n$it")
                    }
                    if (parts[0].isBlank()) {
                        throw IllegalArgumentException("input strings need to have non-blank key before '=', instead got\n$it")
                    }
                    val (k, v) = parts

                    k to v
                }
    }

    override fun apply(settings: Settings) {
        val extraProperties = settings.extensions.extraProperties
        val properties = extraProperties.properties

        fun getProp(
            k: String,
            def: String,
        ) = properties.getOrDefault(k, def).toString()

        fun getPropBool(
            k: String,
            def: String = "true",
        ) = getProp(k, def).toBoolean()

        val dotenvFilename = getProp("dotenv.filename", ".env")

        val dotenvMergeEnvIntoSettingsProperties = getPropBool("dotenv.mergeEnvIntoSettingsProperties")
        val dotenvMergeIntoSettingsProperties = getPropBool("dotenv.mergeIntoSettingsProperties")
        val dotenvMergeEnvIntoProjectProperties = getPropBool("dotenv.mergeEnvIntoProjectProperties")
        val dotenvMergeIntoProjectProperties = getPropBool("dotenv.mergeIntoProjectProperties")

        val extension = settings.gradle.extensions.create(NAME, DotenvPluginExtension::class.java)

        val dotenvFile = settings.settingsDir.resolve(dotenvFilename)
        logger.trace("dotenv file path: ${dotenvFile.path}")

        val vars =
            if (dotenvFile.exists()) {
                parseDotenv(dotenvFile.readLines())
            } else {
                mapOf()
            }

        fun ExtraPropertiesExtension.setFrom(map: Map<String, String>) {
            map.forEach {
                set(it.key, it.value) // needed because properties Map is detached as per doc
            }
        }

        val env = System.getenv()

        if (dotenvMergeEnvIntoSettingsProperties) {
            extraProperties.setFrom(env)
        }

        if (dotenvMergeIntoSettingsProperties) {
            extraProperties.setFrom(vars)
        }

        settings.gradle.beforeProject {
            val projectExtraProperties = it.extra

            if (dotenvMergeEnvIntoProjectProperties) {
                projectExtraProperties.setFrom(env)
            }

            if (dotenvMergeIntoProjectProperties) {
                projectExtraProperties.setFrom(vars)
            }
        }

        extension.vars = vars

        logger.trace("dotenv: {}", vars)
    }
}
