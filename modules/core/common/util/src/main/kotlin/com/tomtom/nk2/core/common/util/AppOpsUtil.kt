/*
 * Copyright (c) 2020 - 2020 TomTom N.V. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom N.V. and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * licensee agreement between you and TomTom. If you are the licensee, you are only permitted
 * to use this Software in accordance with the terms of your license agreement. If you are
 * not the licensee then you are not authorised to use this software in any manner and should
 * immediately return it to TomTom N.V.
 */

package com.tomtom.nk2.core.common.util

import android.app.AppOpsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * Utility class to check app operation (app ops) are allowed.
 */
class AppOpsUtil(context: Context) {

    private val packageManager: PackageManager = context.packageManager

    private val applicationInfo: ApplicationInfo =
        packageManager.getApplicationInfo(context.packageName, 0)

    private val appOpsManager =
        context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager?

    /**
     * Checks if the given app [operation] is allowed.
     *
     * @param operation The operation to check. One of the `AppOpsManager.OP_*` constants.
     *
     * @return `true` when the [operation] is allowed.
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun checkOperation(operation: String) = try {
        appOpsManager!!.unsafeCheckOpNoThrow(
            operation,
            applicationInfo.uid,
            applicationInfo.packageName
        ) == AppOpsManager.MODE_ALLOWED
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}
