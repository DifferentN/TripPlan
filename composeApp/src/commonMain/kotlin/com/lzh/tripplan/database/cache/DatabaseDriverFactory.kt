package com.lzh.tripplan.database.cache

import app.cash.sqldelight.db.SqlDriver

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/2/25
 */
interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

expect fun getDatabaseDriverFactory(): DatabaseDriverFactory