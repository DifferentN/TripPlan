package com.lzh.tripplan.viewmodel.tripdetail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.lzh.tripplan.cache.DAY_SCHEDULE
import com.lzh.tripplan.database.entity.Trip
import com.lzh.tripplan.data.EMPTY_TRIP_ID
import com.lzh.tripplan.database.dao.TripPlanDaoManager
import com.lzh.tripplan.database.entity.DEFAULT_DAY_EVENT_ID
import com.lzh.tripplan.database.entity.DEFAULT_DAY_SCHEDULE_ID
import com.lzh.tripplan.database.entity.DayEvent
import com.lzh.tripplan.database.entity.DayEventDetail
import com.lzh.tripplan.database.entity.DaySchedule
import com.lzh.tripplan.database.entity.EMPTY_TRIP
import com.lzh.tripplan.page.tripdetailpage.TripDetailPageState
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.TripDetailResult
import com.lzh.tripplan.viewmodel.BaseViewModel
import com.lzh.tripplan.event.PageEvent
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.TripDetailTab
import com.lzh.tripplan.page.tripdetailpage.datasource.TripDetailDataSource
import com.lzh.tripplan.page.tripdetailpage.event.AddUpdateEventDetail
import com.lzh.tripplan.page.tripdetailpage.event.AddScheduleEvent
import com.lzh.tripplan.page.tripdetailpage.event.EditTripEvent
import com.lzh.tripplan.page.tripdetailpage.event.CreateDayScheduleEvent
import com.lzh.tripplan.page.tripdetailpage.event.SaveTripEvent
import com.lzh.tripplan.page.tripdetailpage.eventresult.CreateDayScheduleResult
import com.lzh.tripplan.viewmodel.HandlePageEventResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.sin

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/3/8
 */
class TripDetailViewModel: BaseViewModel() {
    val _trip = mutableStateOf<Trip>(EMPTY_TRIP)
    private val _state = mutableStateOf<TripDetailPageState>(TripDetailPageState.EXHIBITION_STATE)
    val trip: State<Trip> = _trip
    val tripDetailPageState: State<TripDetailPageState>
        get() = _state

    private val _tripName = MutableStateFlow<String>("")
    val tripName = _tripName.asStateFlow()

    private val _tripDetailTabList = MutableStateFlow<List<TripDetailTab>>(mutableListOf())
    val tripDetailTabList = _tripDetailTabList.asStateFlow()

    val dataSource = TripDetailDataSource()

    override fun <T : HandlePageEventResult> handlePageEvent(pageEvent: PageEvent, callback: ((T) -> Unit)?) {
        when (pageEvent) {
            is EditTripEvent -> {
                _state.value = TripDetailPageState.EDIT_STATE
            }

            is SaveTripEvent -> {
                _state.value = TripDetailPageState.EXHIBITION_STATE
            }
            is CreateDayScheduleEvent -> {
                createDaySchedule(pageEvent, callback)
            }
            is AddScheduleEvent -> {
                addScheduleEvent(pageEvent)
            }
            is AddUpdateEventDetail -> {
                addEventDetail(pageEvent)
            }

            else -> {}
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
        dataSource.loadTripDetail()
        val tripList = TripPlanDaoManager.queryAllTripOnlyIdName()
        var tripName: String? = ""
        tripList.forEach {
            if (tripId == it.tripId) {
                tripName = it.tripName
            }
        }
        if (tripName.isNullOrEmpty()) {
            return TripDetailResult.FAIL()
        }
        val dayScheduleList = TripPlanDaoManager.queryScheduleIdByTripId(tripId)
        dayScheduleList.forEach { daySchedule ->
            val dayEventList = TripPlanDaoManager.queryEventIdByScheduleId(daySchedule.scheduleId)
            dayEventList.forEach { event ->
                val eventDetailList = TripPlanDaoManager.queryEventDetailByEventId(event.eventId)
                event.detailList = eventDetailList
            }
            daySchedule.dayEventList = dayEventList
        }
        _trip.value = Trip(tripId, tripName, dayScheduleList)
        return TripDetailResult.SUCCESS(_trip.value)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T: HandlePageEventResult> createDaySchedule(event: CreateDayScheduleEvent, callback: ((T) -> Unit)?) {
        viewModelScope.launch {
            val tripId = _trip.value.tripId
            val newDayScheduleName = event.dayScheduleName
            // 创建
            TripPlanDaoManager.createTripSchedule(DAY_SCHEDULE(-1,
                tripId, DEFAULT_DAY_SCHEDULE_ID.toLong(), event.dayScheduleName))
            // 查询
            val dayScheduleList = TripPlanDaoManager.queryScheduleIdByTripId(tripId)
            var dayScheduleId = -1L
            dayScheduleList.forEach { daySchedule ->
                if (daySchedule.scheduleName == newDayScheduleName) {
                    dayScheduleId = daySchedule.scheduleId
                }
            }
            // 结果回调
            if (dayScheduleId < 0) {
                callback?.invoke(CreateDayScheduleResult(dayScheduleId, false) as T)
            } else {
                addNewDayScheduleToTrip(DaySchedule(dayScheduleId, tripId, DEFAULT_DAY_SCHEDULE_ID, newDayScheduleName))
                callback?.invoke(CreateDayScheduleResult(dayScheduleId, true) as T)
            }
        }
    }

    private fun addScheduleEvent(event: AddScheduleEvent) {
        viewModelScope.launch {
            val dayScheduleId = event.dayScheduleId
            TripPlanDaoManager.createTripDayEvent(dayScheduleId, DEFAULT_DAY_EVENT_ID.toLong())
            refreshDayEvent(dayScheduleId)
        }
    }

    private fun addNewDayScheduleToTrip(newDaySchedule: DaySchedule) {
        val oldTrip = _trip.value
        val newDayScheduleList = mutableListOf<DaySchedule>()
        oldTrip.daySchedules?.let {
            newDayScheduleList.addAll(it)
        }
        newDayScheduleList.add(newDaySchedule)
        _trip.value = Trip(oldTrip.tripId, oldTrip.tripName, newDayScheduleList)
    }

    private fun refreshDayEvent(dayScheduleId: Long) {
        viewModelScope.launch {
            val eventList = TripPlanDaoManager.queryEventIdByScheduleId(dayScheduleId)
            val oldTrip = _trip.value
            if (oldTrip.daySchedules == null) {
                return@launch
            }
            var targetDaySchedule: DaySchedule? = null
            for (daySchedule in oldTrip.daySchedules!!) {
                if (daySchedule.scheduleId == dayScheduleId) {
                    targetDaySchedule = daySchedule
                }
            }
            if (targetDaySchedule == null) {
                return@launch
            }
            val newEventList = mutableListOf<DayEvent>()
            val currentEventIdSet = mutableSetOf<Long>()

            targetDaySchedule.dayEventList?.forEach { it ->
                currentEventIdSet.add(it.eventId)
                newEventList.add(it)
            }

            eventList.forEach { event ->
                if (!currentEventIdSet.contains(event.eventId)) {
                    newEventList.add(event)
                }
            }
//            targetDaySchedule.dayEventList = newEventList
            val newDayScheduleList = mutableListOf<DaySchedule>().also {
                oldTrip.daySchedules?.forEach { oldDaySchedule ->
                    if (oldDaySchedule.scheduleId != targetDaySchedule.scheduleId) {
                        it.add(DaySchedule(oldDaySchedule.scheduleId, oldDaySchedule.tripId, oldDaySchedule.schedulePriority,
                            oldDaySchedule.scheduleName, oldDaySchedule.dayEventList))
                    } else {
                        it.add(DaySchedule(oldDaySchedule.scheduleId, oldDaySchedule.tripId, oldDaySchedule.schedulePriority,
                            oldDaySchedule.scheduleName, newEventList))
                    }

                }
            }
            _trip.value = Trip(oldTrip.tripId, "${oldTrip.tripName}", newDayScheduleList)
            var t = 0
            if (_trip.value == oldTrip) {
                t++
            }
            if (_trip.value.daySchedules == oldTrip.daySchedules) {
                t++
            }
            for (index in 0 until oldTrip.daySchedules!!.size) {
                val newDaySchedule = _trip.value.daySchedules?.get(index)
                val oldDaySchedule = oldTrip.daySchedules!!.get(index)
                if (newDaySchedule == oldDaySchedule) {
                    val newEventSize = newDaySchedule.dayEventList?.size ?: 0
                    val oldEventSize = oldDaySchedule.dayEventList?.size ?: 0
                    if (newEventSize == oldEventSize) {
                        t = oldEventSize
                    }
                    if (newDaySchedule.dayEventList == oldDaySchedule.dayEventList) {
                        t++
                    } else {
                        t++
                    }
                } else {
                    t++
                }
            }
        }
    }

    private fun addEventDetail(pageEvent: AddUpdateEventDetail) {
        viewModelScope.launch {
            TripPlanDaoManager.createTripDayDetail(pageEvent.eventId, pageEvent.priority, pageEvent.content)
            refreshEventDetail(pageEvent.eventId)
        }
    }

    private fun refreshEventDetail(eventId: Long) {
        viewModelScope.launch {
            val eventDetailList = TripPlanDaoManager.queryEventDetailByEventId(eventId)
            var targetEvent: DayEvent? = null
            val oldTrip = _trip.value
            if (oldTrip.daySchedules.isNullOrEmpty()) {
                return@launch
            }
            for (daySchedule in oldTrip.daySchedules!!) {
                if (daySchedule.dayEventList.isNullOrEmpty()) {
                    continue
                }
                for (dayEvent in daySchedule.dayEventList!!) {
                    if (dayEvent.eventId == eventId) {
                        targetEvent = dayEvent
                        break
                    }
                }
                if (targetEvent != null) {
                    break
                }
            }
            if (targetEvent == null) {
                return@launch
            }
            var currentDetailIdSet = mutableSetOf<Long>()
            var newEventDetailList = mutableListOf<DayEventDetail>()
            targetEvent.detailList?.forEach {
                currentDetailIdSet.add(it.eventDetailId)
                newEventDetailList.add(it)
            }
            eventDetailList.forEach {
                if (!currentDetailIdSet.contains(it.eventId)) {
                    newEventDetailList.add(it)
                }
            }
            targetEvent.detailList = newEventDetailList
            _trip.value = Trip(oldTrip.tripId, oldTrip.tripName, oldTrip.daySchedules)
        }
    }
}