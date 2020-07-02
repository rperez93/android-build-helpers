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

        val lastTag: String? = try {
            _gitCommands.obtainLastTag()?.extractVersion()
        } catch (e: RuntimeException) {
            if (properties.forceUseOfGitTag) {
                throw e
            }
            null
        }


        if (lastTag != null) {
            println("Version from last git tag: v${lastTag}")
        }

        val currentDateInVersionFormat = getDateInVersionCodeFormat()
        val appVersionName: String? = if (lastTag == null) null else "$lastTag+${currentDateInVersionFormat}"
        val appVersionCode: Int? = if (lastTag == null) null else (currentDateInVersionFormat + properties.buildVersionCodeLastDigit).toInt()


        val plugin = when {
            project.plugins.findPlugin(AndroidApplicationPluginInterface.APP_PLUGIN_ID) != null -> AndroidApplicationPluginInterface.findPluginInProject(project)
            project.plugins.findPlugin(AndroidApplicationPluginInterface.DYNAMIC_FEATURE_PLUGIN_ID) != null -> {
                AndroidApplicationPluginInterface.findPluginInProject(
                    project,
                    AndroidApplicationPluginInterface.DYNAMIC_FEATURE_PLUGIN_ID
                )
            }
            else -> {
                null
            }
        }

        plugin?.apply {
            setVersionName(appVersionName ?: getVersionName())
            setVersionCode(appVersionCode ?: getVersionCode())
        }

    }

}