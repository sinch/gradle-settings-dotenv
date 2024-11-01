package org.gradle.kotlin.dsl

import com.sinch.gradle.dotenv.DotenvPluginExtension
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.plugins.ExtensionAware

val ExtensionAware.dotenv: Map<String, String>
    get() = this.extensions.getByType(DotenvPluginExtension::class.java).vars

val Settings.dotenv: Map<String, String>
    get() = gradle.dotenv

val Project.dotenv: Map<String, String>
    get() = gradle.dotenv
