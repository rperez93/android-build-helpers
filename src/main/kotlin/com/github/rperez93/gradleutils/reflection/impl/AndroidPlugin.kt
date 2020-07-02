package com.github.rperez93.gradleutils.reflection.impl

import com.github.rperez93.gradleutils.reflection.AndroidApplicationPluginInterface
import org.gradle.api.Project

class AndroidPlugin(
    project: Project,
    pluginId: String
) : AndroidApplicationPluginInterface(project, pluginId) {

    private val GET_EXTENSION_METHOD_NAME = "getExtension"

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

    private var _defaultConfig: Any

    init {
        val plugin = project.plugins.findPlugin(pluginId)

        val extension = Class.forName(
            when (pluginId) {
                (DYNAMIC_FEATURE_PLUGIN_ID) -> DYNAMIC_FEATURE_PLUGIN_CLASS_NAME
                else -> APP_PLUGIN_CLASS_NAME
            }
        ).getMethod(GET_EXTENSION_METHOD_NAME).invoke(plugin)
        _defaultConfig = baseExtensionClassGetDefaultConfigMethod.invoke(
            extension
        )
    }

    override fun getVersionName(): String? = defaultProductFlavorClassGetVersionNameMethod.invoke(_defaultConfig) as String?

    override fun setVersionName(value: String?) {
        defaultProductFlavorClassSetVersionNameMethod.invoke(_defaultConfig, value)
    }

    override fun getVersionCode(): Int? = defaultProductFlavorClassGetVersionCodeMethod.invoke(_defaultConfig) as Int?

    override fun setVersionCode(value: Int?) {
        defaultProductFlavorClassSetVersionCodeMethod.invoke(_defaultConfig, value)
    }

}