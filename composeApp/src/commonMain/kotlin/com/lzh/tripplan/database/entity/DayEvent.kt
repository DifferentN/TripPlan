package com.lzh.tripplan.database.entity

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:旅行中某一天中的单个事件
 *
 * @author zhenghaoli
 * @date 2025/2/23
 */
data class DayEvent(
    val eventId: Long,
    val dayScheduleId: Long,
    val eventPriority: Int,
    var detailList: List<DayEventDetail> ?= null
)
