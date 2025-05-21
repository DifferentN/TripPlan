package com.lzh.tripplan

import android.app.Application

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/2/27
 */
class TripPlanApplication: Application() {
    companion object {
        lateinit var application: Application
    }

    init {
        application = this
    }

    override fun onCreate() {
        super.onCreate()
    }
}