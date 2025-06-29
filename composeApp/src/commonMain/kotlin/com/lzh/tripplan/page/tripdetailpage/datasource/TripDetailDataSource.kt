package com.lzh.tripplan.page.tripdetailpage.datasource

import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModelStore
import com.lzh.tripplan.database.dao.TripPlanDaoManager
import com.lzh.tripplan.database.entity.DayEvent
import com.lzh.tripplan.database.entity.DaySchedule
import com.lzh.tripplan.database.entity.EMPTY_TRIP
import com.lzh.tripplan.database.entity.Trip
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

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

    private val LOCK = Mutex()
    private val observerSet = mutableSetOf<TripDetailDataObserver>()
    /**
     * 查找Trip
     */
    suspend fun loadTripDetail(tripId: Long): Trip {
        trip = EMPTY_TRIP
        val tripList = TripPlanDaoManager.queryAllTripOnlyIdName()
        if (tripList.isEmpty()) {
            return EMPTY_TRIP
        }
        for (tripItem in tripList) {
            if (tripItem.tripId == tripId) {
                trip = tripItem
                break
            }
        }

        val dayScheduleList = TripPlanDaoManager.queryScheduleIdByTripId(tripId)
        if (dayScheduleList.isEmpty()) {
            return trip
        }
        dayScheduleList.forEach {
            loadAndFillDaySchedule(it)
        }
        trip.daySchedules = dayScheduleList
        return trip
    }

    suspend fun loadAndFillDaySchedule(daySchedule: DaySchedule) {
        val eventList = TripPlanDaoManager.queryEventIdByScheduleId(daySchedule.scheduleId)
        daySchedule.dayEventList = eventList
        daySchedule.dayEventList?.forEach {
            loadAndFillDayEvent(it)
        }
    }

    suspend fun loadAndFillDayEvent(dayEvent: DayEvent) {
        val detailList = TripPlanDaoManager.queryEventDetailByEventId(dayEvent.eventId)
        dayEvent.detailList = detailList
    }

    suspend fun subscribe(observer: TripDetailDataObserver) {
        LOCK.withLock {
            observerSet.add(observer)
        }
    }

    suspend fun unsubscribe(observer: TripDetailDataObserver) {
        LOCK.withLock {
            observerSet.remove(observer)
        }
    }

    fun obtainDaySchedule(dayId: Long): DaySchedule? {
        val daySchedule: DaySchedule? = trip.daySchedules?.filter { it.scheduleId == dayId }?.getOrNull(0)
        return daySchedule
    }

    fun notifyTripDataChanged() {
        observerSet.forEach {
            it.onTripDetailDataChanged()
        }
    }
}

val EMPTY_TRIP_DETAIL_DATA_SOURCE = TripDetailDataSource()
val LocalTripDetailDataSources = compositionLocalOf { EMPTY_TRIP_DETAIL_DATA_SOURCE }
val TRIP_DETAIL_VIEW_MODEL_STORE = ViewModelStore()
val LocalTripDetailViewModelStore = compositionLocalOf { TRIP_DETAIL_VIEW_MODEL_STORE }