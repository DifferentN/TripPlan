package com.lzh.tripplan.page.tripdetailpage.datasource

import androidx.compose.runtime.compositionLocalOf
import com.lzh.tripplan.database.entity.EMPTY_TRIP
import com.lzh.tripplan.database.entity.Trip

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/5/14
 */
class TripDetailDataSource {
    var trip: Trip = EMPTY_TRIP
        private set
    var tripName: String = ""
        private set

    fun loadTripDetail() {
    }
}

val EMPTY_TRIP_DETAIL_DATA_SOURCE = TripDetailDataSource()
val LocalTripDetailDataSources = compositionLocalOf { EMPTY_TRIP_DETAIL_DATA_SOURCE }