/*
 * Copyright 2019 Rafael Pérez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.rperez93.gradleutils

import com.github.rperez93.gradleutils.git.GitCommands
import com.github.rperez93.gradleutils.git.GitCommands.Companion.extractVersion
import com.github.rperez93.gradleutils.helper.getDateInVersionCodeFormat
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.lang.RuntimeException

class AndroidProjectVersionFromGitTagPlugin : Plugin<Project> {

    companion object {
        const val PLUGIN_EXTENSION = "projectVersionFromGitTag"
    }

    @Suppress("MemberVisibilityCanBePrivate")
    open class VersionInfo(
        val versionName: String?,
        val currentDateInVersionFormat: String?
    ) {

        var versionCodeLastDigit: Int = 0
        val versionCode: Int
            get() {
                if (currentDateInVersionFormat == null)
                    return 0
                return (currentDateInVersionFormat + versionCodeLastDigit).toInt()
            }
    }

    private lateinit var _gitCommands: GitCommands

    override fun apply(project: Project) {

        _gitCommands = GitCommands(project)

        val lastTag: String? = try {
            _gitCommands.obtainLastTag()?.extractVersion()
        } catch (e: RuntimeException) {
            null
        }

        if (lastTag != null) {
            println("[AppVersionFromGitTag] Version from last git tag: v${lastTag}")
        }

        val currentDateInVersionFormat = getDateInVersionCodeFormat()
        val appVersionName: String? = if (lastTag == null) null else "$lastTag+${currentDateInVersionFormat}"


        project.extensions.create(
            PLUGIN_EXTENSION,
            VersionInfo::class.java, appVersionName,
            currentDateInVersionFormat)

    }

}