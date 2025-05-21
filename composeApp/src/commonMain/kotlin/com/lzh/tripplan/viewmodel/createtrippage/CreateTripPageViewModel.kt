package com.lzh.tripplan.viewmodel.createtrippage

import com.lzh.tripplan.database.dao.TripPlanDaoManager
import com.lzh.tripplan.viewmodel.BaseViewModel
import com.lzh.tripplan.event.PageEvent
import com.lzh.tripplan.event.PageEventHandleResponse
import com.lzh.tripplan.page.createtrippage.event.CreateTripNameEvent
import com.lzh.tripplan.viewmodel.HandlePageEventResult

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/3/22
 */
class CreateTripPageViewModel: BaseViewModel() {
    override fun <T : HandlePageEventResult> handlePageEvent(pageEvent: PageEvent, callback: ((T) -> Unit)?) {
        when {
            pageEvent is CreateTripNameEvent -> {

            }
            else -> {
            }
        }
    }

    override suspend fun <T : HandlePageEventResult> syncHandlePageEvent(pageEvent: PageEvent): T? {
        return null
    }

    suspend fun createTrip(tripName: String): CreateTripResult {
        TripPlanDaoManager.createTrip(tripName)
        val tripList = TripPlanDaoManager.queryAllTripOnlyIdName()
        var tripId = -1L
        tripList.forEach {
            // 如果名称相同，选择tripId最大的一个
            if (tripName == it.tripName) {
//                tripId = max(tripId.toULong(), it.tripId.toULong()).toLong()
                if (tripId < it.tripId) {
                    tripId = it.tripId
                }
            }
        }
        val res = if (tripId != -1L) {
            CreateTripResult(true, tripId)
        } else {
            CreateTripResult(false, tripId)
        }
        return res
    }
}