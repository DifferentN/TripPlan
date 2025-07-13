package com.lzh.tripplan.viewmodel.tripdetail

import androidx.lifecycle.viewModelScope
import com.lzh.tripplan.database.entity.DayEvent
import com.lzh.tripplan.event.PageEvent
import com.lzh.tripplan.page.tripdetailpage.datasource.TripDetailDataSource
import com.lzh.tripplan.viewmodel.BaseViewModel
import com.lzh.tripplan.viewmodel.HandlePageEventResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/7/9
 */
class TripDayEventViewModel(val dayScheduleId: Long, val dayEventId: Long, val dataSource: TripDetailDataSource): BaseViewModel() {
    private val EMPTY_DAY_EVENT_NAME = "Input day event name"
    private val EMPTY_DAY_EVENT_CONTENT = "Input day event content"
    // 日程事件名称
    private val _dayEventName = MutableStateFlow("")
    val dayEventName = _dayEventName.asStateFlow()
    // 日程事件内容
    private val _dayEventContent = MutableStateFlow("")
    val dayEventContent = _dayEventContent.asStateFlow()

    private var resultEventName = ""
    private var resultEventContent = ""

    private var dayEventExist = false
    private var dayEvent: DayEvent? = null

    init {
        dayEvent = dataSource.obtainDayEvent(dayScheduleId, dayEventId)
        if (dayEvent != null) {
            dayEventExist = true
        }
        _dayEventName.value = extractEventName(dayEvent)
        _dayEventContent.value = extractEventContent(dayEvent)
    }

    fun saveDayEvent() {
        viewModelScope.launch {
            val nameDetailId = obtainEventNameDetailId(dayEvent)
            val contentDetailId = obtainEventContentDetailId(dayEvent)
            val eventId = dayEvent?.eventId ?: -1
            if (eventId > 0) {
                dataSource.upsertEventDetail(eventId, 2, resultEventName, nameDetailId)
                dataSource.upsertEventDetail(eventId, 1, resultEventName, contentDetailId)
            } else {
                val eventId = dataSource.createNewDayEvent(dayScheduleId)
                if (eventId < 0) {
                    return@launch
                }
                dataSource.upsertEventDetail(eventId, 2, resultEventName, nameDetailId)
                dataSource.upsertEventDetail(eventId, 1, resultEventName, contentDetailId)
                dayEvent = dataSource.obtainDayEvent(dayScheduleId, eventId)
            }
        }
    }

    fun updateDayEventName(dayEventName: String) {
        resultEventName = dayEventName
    }

    fun updateDayEventContent(dayEventContent: String) {
        resultEventContent = dayEventContent
    }

    override fun <T : HandlePageEventResult> handlePageEvent(
        pageEvent: PageEvent,
        callback: ((T) -> Unit)?
    ) {

    }

    override suspend fun <T : HandlePageEventResult> syncHandlePageEvent(pageEvent: PageEvent): T? {
        return null
    }

    private fun extractEventName(dayEvent: DayEvent?): String {
        if (dayEvent == null) {
            return EMPTY_DAY_EVENT_NAME
        }
        val dayDetail = dayEvent.detailList?.getOrNull(0)
        val dayEventName = if (dayDetail?.content.isNullOrEmpty()) {
            EMPTY_DAY_EVENT_NAME
        } else {
            dayDetail.content
        }
        return dayEventName
    }

    private fun extractEventContent(dayEvent: DayEvent?): String {
        if (dayEvent == null) {
            return EMPTY_DAY_EVENT_CONTENT
        }
        val dayDetail = dayEvent.detailList?.getOrNull(1)
        val dayEventContent = if (dayDetail?.content.isNullOrEmpty()) {
            EMPTY_DAY_EVENT_CONTENT
        } else {
            dayDetail.content
        }
        return dayEventContent
    }

    private fun obtainEventNameDetailId(dayEvent: DayEvent?): Long {
        return dayEvent?.detailList?.getOrNull(0)?.eventDetailId ?: -1
    }

    private fun obtainEventContentDetailId(dayEvent: DayEvent?): Long {
        return dayEvent?.detailList?.getOrNull(1)?.eventDetailId ?: -1
    }
}