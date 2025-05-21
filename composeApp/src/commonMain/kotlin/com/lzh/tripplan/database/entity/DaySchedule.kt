package com.lzh.tripplan.database.entity

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述: 旅行中某一天的计划
 *
 * @author zhenghaoli
 * @date 2025/2/23
 */
data class DaySchedule(
    val scheduleId: Long,
    val tripId: Long,
    val schedulePriority: Int,
    val scheduleName: String,
    var dayEventList: List<DayEvent>? = null
)
