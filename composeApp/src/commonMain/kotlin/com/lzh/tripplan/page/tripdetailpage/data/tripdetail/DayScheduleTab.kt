package com.lzh.tripplan.page.tripdetailpage.data.tripdetail

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/3/14
 */
data class DayScheduleTab(
    val type: DetailPageDayTabType,
    val dayScheduleId: Long,
    val tabName: String
)

enum class DetailPageDayTabType {
    EXHIBITION_TYPE,
    ADD_TYPE
}
val ADD_TAB_TYPE_ID = -1L
val ADD_TAB_TYPE_NAME = "Add Schedule"
