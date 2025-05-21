package com.lzh.tripplan.database.cache

actual fun getDatabaseDriverFactory(): DatabaseDriverFactory {
    return AndroidDatabaseDriverFactory
}