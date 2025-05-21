package com.lzh.tripplan.database.cache

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.lzh.tripplan.TripPlanApplication
import com.lzh.tripplan.cache.AppDatabase

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/2/25
 */
object AndroidDatabaseDriverFactory: DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(AppDatabase.Schema, TripPlanApplication.application, "launch.db")
    }
}