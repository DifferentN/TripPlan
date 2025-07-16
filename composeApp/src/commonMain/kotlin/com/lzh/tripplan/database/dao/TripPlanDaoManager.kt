package com.lzh.tripplan.database.dao

import com.lzh.tripplan.cache.DAY_SCHEDULE
import com.lzh.tripplan.database.DatabaseManager
import com.lzh.tripplan.database.entity.DayEvent
import com.lzh.tripplan.database.entity.DayEventDetail
import com.lzh.tripplan.database.entity.DaySchedule
import com.lzh.tripplan.database.entity.Trip

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/2/27
 */
object TripPlanDaoManager {
    suspend fun queryAllTripOnlyIdName(): List<Trip> {
        return DatabaseManager.dbQuery.selectAllTrip(::mapTrip).executeAsList()
    }

    suspend fun queryScheduleIdByTripId(tripId: Long): List<DaySchedule> {
        return DatabaseManager.dbQuery
            .selectAllDaySchedule(tripId, ::mapDaySchedule).executeAsList()
    }

    suspend fun queryEventIdByScheduleId(scheduleId: Long): List<DayEvent> {
        return DatabaseManager.dbQuery
            .selectAllDayEvent(scheduleId, ::mapDayEvent).executeAsList()
    }

    suspend fun queryEventDetailByEventId(eventId: Long): List<DayEventDetail> {
        return DatabaseManager.dbQuery
            .selectAllEventDetails(eventId, ::mapDayEventDetail).executeAsList()
    }

    suspend fun createTrip(tripName: String) {
        return DatabaseManager.dbQuery.insertTrip(tripName)
    }

    suspend fun createTripSchedule(daySchedule: DAY_SCHEDULE) {
        return DatabaseManager.dbQuery.insertDaySchedule(daySchedule)
    }

    suspend fun createTripDayEvent(dayScheduleId: Long, priority: Long) {
        return DatabaseManager.dbQuery.insertDayEvent(dayScheduleId, priority)
    }

    suspend fun createTripDayDetail(eventId: Long, priority: Long, content: String) {
        return DatabaseManager.dbQuery.insertEventDetail(eventId, priority, content)
    }

    suspend fun updateTripDayDetail(eventId: Long, priority: Long?, content: String, eventDetailId: Long) {
        return DatabaseManager.dbQuery.updateEventDetail(eventId, priority, content, eventDetailId)
    }

    private fun mapTrip(id: Long, name: String?): Trip {
        return Trip(id, name, null)
    }

    private fun mapDaySchedule(scheduleId: Long, tripId: Long?,
        priority: Long?, name: String?): DaySchedule {
        return DaySchedule(scheduleId, tripId ?:0L, priority?.toInt() ?: 0, name ?:"")
    }

    private fun mapDayEvent(dayEventId: Long, scheduleId: Long?, priority: Long?): DayEvent {
        return DayEvent(dayEventId, scheduleId ?: 0, priority?.toInt() ?: 0,)
    }

    private fun mapDayEventDetail(eventDetailId: Long,
        eventId: Long?, priority: Long?, content: String?): DayEventDetail {
        return DayEventDetail(eventDetailId, eventId ?: 0,
            priority?.toInt() ?: 0, content ?: "")
    }
}