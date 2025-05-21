package com.lzh.tripplan.database.cache

import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual fun getDatabaseDriverFactory(): DatabaseDriverFactory {
    return IOSDatabaseDriverFactory
}