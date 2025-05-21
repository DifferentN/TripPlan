package com.lzh.tripplan

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform