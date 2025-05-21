package com.lzh.tripplan.database.entity

import com.lzh.tripplan.data.EMPTY_TRIP_ID

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述: 单个旅行计划
 *
 * @author zhenghaoli
 * @date 2025/2/23
 */
data class Trip(
    val tripId: Long,
    val tripName: String?,
    var daySchedules: List<DaySchedule>?
) {
}

val EMPTY_TRIP = Trip(EMPTY_TRIP_ID, "", null)

