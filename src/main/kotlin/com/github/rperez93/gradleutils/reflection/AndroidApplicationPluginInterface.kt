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

import org.gradle.api.Project

@Suppress("MemberVisibilityCanBePrivate")
class AndroidApplicationPluginInterface(
    private var _defaultConfig: Any
) {

    var versionName: String?
        get() = defaultProductFlavorClassGetVersionNameMethod.invoke(_defaultConfig) as String?
        set(value) {
            defaultProductFlavorClassSetVersionNameMethod.invoke(_defaultConfig, value)
        }

    var versionCode: Int?
        get() = defaultProductFlavorClassGetVersionCodeMethod.invoke(_defaultConfig) as Int?
        set(value) {
            defaultProductFlavorClassSetVersionCodeMethod.invoke(_defaultConfig, value)
        }

    companion object {

        const val APP_PLUGIN_ID = "com.android.application"
        const val DYNAMIC_FEATURE_PLUGIN_ID = "com.android.dynamic-feature"

        const val APP_PLUGIN_CLASS_NAME = "com.android.build.gradle.AppPlugin"
        const val DYNAMIC_FEATURE_PLUGIN_CLASS_NAME = "com.android.build.gradle.DynamicFeaturePlugin"

        private const val GET_EXTENSION_METHOD_NAME = "getExtension"

        private val baseExtensionClass = Class.forName("com.android.build.gradle.BaseExtension")
        private val baseExtensionClassGetDefaultConfigMethod = baseExtensionClass.getMethod("getDefaultConfig")

        private val defaultProductFlavorClass = Class.forName("com.android.builder.core.DefaultProductFlavor")
        private val defaultProductFlavorClassSetVersionNameMethod =
            defaultProductFlavorClass.getDeclaredMethod("setVersionName", String::class.java)
        private val defaultProductFlavorClassSetVersionCodeMethod =
            defaultProductFlavorClass.getDeclaredMethod("setVersionCode", Integer::class.java)
        private val defaultProductFlavorClassGetVersionNameMethod =
            defaultProductFlavorClass.getDeclaredMethod("getVersionName")
        private val defaultProductFlavorClassGetVersionCodeMethod =
            defaultProductFlavorClass.getDeclaredMethod("getVersionCode")

        fun findPluginInProject(project: Project, pluginId: String = APP_PLUGIN_ID) : AndroidApplicationPluginInterface? {
            val plugin = project.plugins.findPlugin(APP_PLUGIN_ID) ?: return null
            val extension = Class.forName(
                when(pluginId) {
                    (DYNAMIC_FEATURE_PLUGIN_ID) -> DYNAMIC_FEATURE_PLUGIN_CLASS_NAME
                    else -> APP_PLUGIN_CLASS_NAME
                }
            ).getMethod(GET_EXTENSION_METHOD_NAME).invoke(plugin)
            return AndroidApplicationPluginInterface(
                baseExtensionClassGetDefaultConfigMethod.invoke(
                    extension
                )
            )
        }

    }

}