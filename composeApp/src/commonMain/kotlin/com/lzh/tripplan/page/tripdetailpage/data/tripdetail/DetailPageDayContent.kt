package com.lzh.tripplan.page.tripdetailpage.data.tripdetail

import com.lzh.tripplan.database.entity.DaySchedule

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/3/12
 */
data class DetailPageDayContent(val type: DetailPageDayContentType,
    val daySchedule: DaySchedule? = null,
    val addDayScheduleItem: AddDayScheduleItem? = null)

enum class DetailPageDayContentType {
    EXHIBITION_TYPE,
    ADD_TYPE
}