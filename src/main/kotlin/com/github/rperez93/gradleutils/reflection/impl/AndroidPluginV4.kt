package com.github.rperez93.gradleutils.reflection.impl

import com.github.rperez93.gradleutils.reflection.AndroidApplicationPluginInterface
import org.gradle.api.Project
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

class AndroidPluginV4(project: Project, pluginId: String) : AndroidApplicationPluginInterface(project, pluginId) {

    private val GET_EXTENSION_METHOD_NAME = "getExtension"

    private val BASE_EXTENSION_CLASS = Class.forName("com.android.build.gradle.BaseExtension")

    private val ABSTRACT_PRODUCT_FLAVOR_INTERFACE = Class.forName("com.android.builder.core.AbstractProductFlavor")

    private val DEFAULT_CONFIG_GET_VERSION_CODE =
        ABSTRACT_PRODUCT_FLAVOR_INTERFACE.getDeclaredMethod("getVersionCode")
    private val DEFAULT_CONFIG_GET_VERSION_NAME =
        ABSTRACT_PRODUCT_FLAVOR_INTERFACE.getDeclaredMethod("getVersionName")

    private val DEFAULT_CONFIG_SET_VERSION_CODE =
        ABSTRACT_PRODUCT_FLAVOR_INTERFACE.getDeclaredMethod("setVersionCode", Integer::class.java)

    private val DEFAULT_CONFIG_SET_VERSION_NAME =
        ABSTRACT_PRODUCT_FLAVOR_INTERFACE.getDeclaredMethod("setVersionName", String::class.java)


    private var _defaultConfig: Any? = null

    init {
        val currentPlugin = project.plugins.findPlugin(pluginId)
        val extension = Class.forName(
            when (pluginId) {
                (DYNAMIC_FEATURE_PLUGIN_ID) -> DYNAMIC_FEATURE_PLUGIN_CLASS_NAME
                else -> APP_PLUGIN_CLASS_NAME
            }
        ).getMethod(GET_EXTENSION_METHOD_NAME).invoke(currentPlugin)

        extension::class.declaredMemberProperties.forEach {
            if (it.name == "defaultConfig") {
                @Suppress("UNCHECKED_CAST")
                _defaultConfig = (it as KProperty1<Any, Any>).get(extension)
            }
        }
    }


    override fun getVersionName(): String? {
        return DEFAULT_CONFIG_GET_VERSION_NAME.invoke(_defaultConfig) as String?
    }

    override fun setVersionName(value: String?) {
        DEFAULT_CONFIG_SET_VERSION_NAME.invoke(_defaultConfig, value)
    }

    override fun getVersionCode(): Int? {
        return DEFAULT_CONFIG_GET_VERSION_CODE.invoke(_defaultConfig) as Int?
    }

    override fun setVersionCode(value: Int?) {
       DEFAULT_CONFIG_SET_VERSION_CODE(_defaultConfig, value)
    }
}