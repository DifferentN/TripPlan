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