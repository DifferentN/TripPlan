package com.lzh.tripplan.viewmodel.tripdetail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.node.WeakReference
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
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.TRIP_DETAIL_ADD_TAB
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.TRIP_DETAIL_PREFIX
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.TRIP_DETAIL_SUMMARY_TAB
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.TripDetailTab
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.TripDetailTabType
import com.lzh.tripplan.page.tripdetailpage.datasource.TripDetailDataSource
import com.lzh.tripplan.page.tripdetailpage.event.CreateDayEventEvent
import com.lzh.tripplan.page.tripdetailpage.event.CreateDayScheduleEvent
import com.lzh.tripplan.viewmodel.HandlePageEventResult
import com.lzh.tripplan.viewmodel.IPageHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.Navigator

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

    private var navigator: Navigator? = null

    override fun <T : HandlePageEventResult> handlePageEvent(pageEvent: PageEvent, callback: ((T) -> Unit)?) {
        when {
            pageEvent is CreateDayEventEvent -> {
                jumpToCreateDayEventPage(pageEvent)
            }
            else -> {
            }
        }
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

        // 设置trip名称
        _tripName.value = trip.tripName ?: ""

        // 添加tripDetailTab
        val tabList = mutableListOf<TripDetailTab>()
        tabList.add(TripDetailTab(TRIP_DETAIL_SUMMARY_TAB, TripDetailTabType.SUMMARY, "概览", -1))
        trip.daySchedules?.forEach {
            tabList.add(TripDetailTab("${TRIP_DETAIL_PREFIX}_${it.scheduleId}", TripDetailTabType.DAY_TAB, it.scheduleName, it.scheduleId))
        }
        tabList.add(TripDetailTab(TRIP_DETAIL_ADD_TAB, TripDetailTabType.ADD_TAB, "添加", -1))
        _tripDetailTabList.value = tabList

        return TripDetailResult.SUCCESS()
    }

    /**
     * 创建一个新的daySchedule
     */
    fun createNewDaySchedule() {
        viewModelScope.launch {
            val tripId = _trip.value.tripId
            if (tripId == EMPTY_TRIP_ID) {
                return@launch
            }
            dataSource.createNewDaySchedule()
            // 重新加载 trip
            loadTrip(tripId)
        }
    }

    fun updatePageHandler(pageHandler: IPageHandler?) {
        nextHandler = pageHandler
    }

    fun updateNavigator(navigator: Navigator) {
        this.navigator = navigator
    }

    private fun jumpToCreateDayEventPage(event: CreateDayEventEvent) {
        navigator?.navigate("${event.pageId}/${event.dayScheduleId}/${event.dayEventId}")
    }

    private fun <T: HandlePageEventResult> createDaySchedule(event: CreateDayScheduleEvent, callback: ((T) -> Unit)?) {

    }

}