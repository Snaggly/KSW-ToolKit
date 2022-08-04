package com.snaggly.ksw_toolkit.util.list.app

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("QueryPermissionsNeeded")
class AppsLister (context: Context) {
    private val appsList = ArrayList<AppInfo>()

    init {
        val apps = context.packageManager.getInstalledPackages(0)

        for (packageInfo in apps){
            if (context.packageManager.getLaunchIntentForPackage(packageInfo.packageName) == null) {
                continue;
            }
            val thisAppInfo = AppInfo(
                    packageInfo.applicationInfo.loadLabel(context.packageManager) as String,
                    packageInfo.packageName,
                    packageInfo.applicationInfo.loadIcon(context.packageManager)
            )

            appsList.add(thisAppInfo)
        }

        appsList.sortBy { it.name }
    }

    fun getNumOfApps() : Int{
        return appsList.size
    }

    fun getAppsList() : ArrayList<AppInfo> {
        return appsList
    }
}