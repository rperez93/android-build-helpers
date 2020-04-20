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

import org.gradle.api.Project
import java.io.FileInputStream
import java.util.*

class PluginProperties (project: Project){
    private val _properties = Properties()

    init {
        val file = project.rootProject.file("local.properties")
        if (file.exists()) {
            _properties.load(FileInputStream(file))
        }
    }

    val forceUseOfGitTag: Boolean
        get() = (_properties[FORCE_USE_OF_GIT] as String?)?.toBoolean() ?: false

    val buildVersionCodeLastDigit: Int
        get() = (_properties[VERSION_CODE_LAST_DIGIT] as String?)?.toInt() ?: 1

    companion object {
        const val FORCE_USE_OF_GIT = "gradle-utils.forceUseOfGitVersion"
        const val VERSION_CODE_LAST_DIGIT = "gradle-utils.buildVersionCodeLastDigit"
    }

}