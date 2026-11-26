package com.fufu.quiz.util

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager

object LauncherIconSwitcher {
    private const val DEFAULT_ALIAS = "com.fufu.quiz.DefaultLauncher"
    private const val FUFU_ALIAS = "com.fufu.quiz.FufuLauncher"

    fun applyFufuMode(context: Context, enabled: Boolean) {
        val appContext = context.applicationContext
        val packageManager = appContext.packageManager
        val defaultComponent = ComponentName(appContext.packageName, DEFAULT_ALIAS)
        val fufuComponent = ComponentName(appContext.packageName, FUFU_ALIAS)

        runCatching {
            if (enabled) {
                setEnabled(packageManager, fufuComponent, true)
                setEnabled(packageManager, defaultComponent, false)
            } else {
                setEnabled(packageManager, defaultComponent, true)
                setEnabled(packageManager, fufuComponent, false)
            }
        }
    }

    private fun setEnabled(
        packageManager: PackageManager,
        componentName: ComponentName,
        enabled: Boolean
    ) {
        val targetState = if (enabled) {
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        } else {
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        }
        if (packageManager.getComponentEnabledSetting(componentName) == targetState) return
        packageManager.setComponentEnabledSetting(
            componentName,
            targetState,
            PackageManager.DONT_KILL_APP
        )
    }
}
