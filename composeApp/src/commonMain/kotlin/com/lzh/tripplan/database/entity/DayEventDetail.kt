package com.lzh.tripplan.database.entity

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:旅行中某一天中单个事件的详细说明
 *
 * @author zhenghaoli
 * @date 2025/2/23
 */
data class DayEventDetail(
    val eventDetailId: Long,
    val eventId: Long,
    val eventDetailPriority: Int,
    val content: String
)
