package com.lzh.tripplan.page.tripdetailpage.data.tripdetail

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/5/14
 */
data class TripDetailTab(
    val tabId: String,
    val tabType: TripDetailTabType,
    val tabName: String,
    val dayId: Long)

enum class TripDetailTabType {
    SUMMARY,
    DAY_TAB,
    ADD_TAB
}

const val TRIP_DETAIL_SUMMARY_TAB = "TRIP_DETAIL_TAB_SUMMARY"
const val TRIP_DETAIL_PREFIX = "TRIP_DETAIL_TAB_PREFIX"
const val TRIP_DETAIL_ADD_TAB = "TRIP_DETAIL_ADD_TAB"
