package com.lzh.tripplan.data

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/2/11
 */
data class TripDigestItem(val tripId: Int,
    val title: String,
    val launchTime: Long,
    val endTime: Long)
