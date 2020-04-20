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
import com.github.rperez93.gradleutils.reflection.AndroidApplicationPluginInterface
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.lang.RuntimeException

class AndroidProjectVersionFromGitTagPlugin : Plugin<Project> {

    private val _gitCommands = GitCommands()

    override fun apply(project: Project) {

        val properties = PluginProperties(project)

        val lastTag: String = try {
            _gitCommands.obtainLastTag()?.extractVersion()
        } catch (e: RuntimeException) {
            if (properties.forceUseOfGitTag) {
                throw e
            }
            null
        } ?: "0.0.0"

        val currentDateInVersionFormat = getDateInVersionCodeFormat()
        val appVersionName: String? = "$lastTag+${currentDateInVersionFormat}"
        val appVersionCode: Int? = (currentDateInVersionFormat + properties.buildVersionCodeLastDigit).toInt()

        AndroidApplicationPluginInterface.findPluginInProject(project)?.apply {
            versionName = versionName ?: appVersionName
            versionCode = versionCode ?: appVersionCode
        }

        AndroidApplicationPluginInterface.findPluginInProject(
            project,
            AndroidApplicationPluginInterface.DYNAMIC_FEATURE_PLUGIN_ID
        )?.apply {
            versionName = versionName ?: appVersionName
            versionCode = versionCode ?: appVersionCode
        }

    }

}