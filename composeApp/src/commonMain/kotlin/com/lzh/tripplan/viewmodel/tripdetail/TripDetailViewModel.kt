package com.lzh.tripplan.viewmodel.tripdetail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.lzh.tripplan.cache.DAY_SCHEDULE
import com.lzh.tripplan.database.entity.Trip
import com.lzh.tripplan.data.EMPTY_TRIP_ID
import com.lzh.tripplan.database.dao.TripPlanDaoManager
import com.lzh.tripplan.database.entity.EMPTY_TRIP
import com.lzh.tripplan.page.tripdetailpage.TripDetailPageState
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.TripDetailResult
import com.lzh.tripplan.viewmodel.BaseViewModel
import com.lzh.tripplan.event.PageEvent
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.TripDetailTab
import com.lzh.tripplan.page.tripdetailpage.datasource.TripDetailDataSource
import com.lzh.tripplan.page.tripdetailpage.event.CreateDayScheduleEvent
import com.lzh.tripplan.viewmodel.HandlePageEventResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/3/8
 */
class TripDetailViewModel: BaseViewModel() {
    val _trip = mutableStateOf<Trip>(EMPTY_TRIP)
    val trip: State<Trip> = _trip

    private val _tripName = MutableStateFlow<String>("")
    val tripName = _tripName.asStateFlow()

    private val _tripDetailTabList = MutableStateFlow<List<TripDetailTab>>(mutableListOf())
    val tripDetailTabList = _tripDetailTabList.asStateFlow()

    val dataSource = TripDetailDataSource()

    override fun <T : HandlePageEventResult> handlePageEvent(pageEvent: PageEvent, callback: ((T) -> Unit)?) {

    }

    override suspend fun <T : HandlePageEventResult> syncHandlePageEvent(pageEvent: PageEvent): T? {
        return null
    }

    /**
     * 根据TripId加载Trip
     * @param tripId Long
     * @return TripDetailResult
     */
    suspend fun loadTrip(tripId: Long): TripDetailResult {
        val trip = dataSource.loadTripDetail(tripId)
        _trip.value = trip
        return TripDetailResult.SUCCESS()
    }

    private fun <T: HandlePageEventResult> createDaySchedule(event: CreateDayScheduleEvent, callback: ((T) -> Unit)?) {

    }

}