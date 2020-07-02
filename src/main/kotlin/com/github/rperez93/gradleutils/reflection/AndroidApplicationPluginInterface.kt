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

package com.github.rperez93.gradleutils.reflection

import com.github.rperez93.gradleutils.reflection.impl.AndroidPlugin
import com.github.rperez93.gradleutils.reflection.impl.AndroidPluginV4
import org.gradle.api.Project

@Suppress("MemberVisibilityCanBePrivate")
abstract class AndroidApplicationPluginInterface(
    protected val project: Project,
    protected val pluginId: String
) {

    abstract fun getVersionName(): String?
    abstract fun setVersionName(value: String?)
    abstract fun getVersionCode(): Int?
    abstract fun setVersionCode(value: Int?)

    companion object {

        const val APP_PLUGIN_CLASS_NAME = "com.android.build.gradle.AppPlugin"
        const val DYNAMIC_FEATURE_PLUGIN_CLASS_NAME = "com.android.build.gradle.DynamicFeaturePlugin"


        const val APP_PLUGIN_ID = "com.android.application"
        const val DYNAMIC_FEATURE_PLUGIN_ID = "com.android.dynamic-feature"

        fun findPluginInProject(
            project: Project,
            pluginId: String = APP_PLUGIN_ID
        ): AndroidApplicationPluginInterface? {
            return try {
                AndroidPlugin(project, pluginId)
            } catch (ignored: Exception) {
                AndroidPluginV4(project, pluginId)
            }
        }

    }

}