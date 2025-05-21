package com.lzh.tripplan.database

import com.lzh.tripplan.cache.AppDatabase
import com.lzh.tripplan.cache.AppDatabaseQueries
import com.lzh.tripplan.database.cache.getDatabaseDriverFactory

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/2/27
 */
object DatabaseManager {
    private lateinit var database: AppDatabase
    lateinit var dbQuery: AppDatabaseQueries
        private set
    private var hasInit = false

    fun initDatabaseManager() {
        if (hasInit) {
            return
        }
        database = AppDatabase(getDatabaseDriverFactory().createDriver())
        dbQuery = database.appDatabaseQueries
        hasInit = true
    }
}