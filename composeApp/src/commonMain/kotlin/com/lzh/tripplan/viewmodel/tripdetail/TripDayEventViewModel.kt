package com.lzh.tripplan.viewmodel.tripdetail

import androidx.lifecycle.viewModelScope
import com.lzh.tripplan.database.entity.DayEvent
import com.lzh.tripplan.database.entity.DayEventDetail
import com.lzh.tripplan.event.PageEvent
import com.lzh.tripplan.page.tripdetailpage.datasource.TripDetailDataSource
import com.lzh.tripplan.utils.EventDetailUtils
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
        _dayEventName.value = EventDetailUtils.obtainEventNameDetailContent(dayEvent)
        _dayEventContent.value = EventDetailUtils.obtainEventContentDetailContent(dayEvent)
    }

    fun saveDayEvent() {
        viewModelScope.launch {
            val nameDetailId = EventDetailUtils.obtainEventNameDetailId(dayEvent)
            val contentDetailId = EventDetailUtils.obtainEventContentDetailId(dayEvent)
            val eventId = dayEvent?.eventId ?: -1
            if (eventId > 0) {
                dataSource.updateEventDetail(eventId, 2, resultEventName, nameDetailId)
                dataSource.updateEventDetail(eventId, 1, resultEventContent, contentDetailId)
            } else {
                val eventId = dataSource.createNewDayEvent(dayScheduleId)
                if (eventId < 0) {
                    return@launch
                }
                dataSource.createEventDetail(eventId, 2, resultEventName)
                dataSource.createEventDetail(eventId, 1, resultEventContent)
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


}