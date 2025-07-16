package com.lzh.tripplan.viewmodel.tripdetail

import androidx.lifecycle.viewModelScope
import com.lzh.tripplan.database.entity.DayEvent
import com.lzh.tripplan.database.entity.DaySchedule
import com.lzh.tripplan.event.PageEvent
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.DayEventExhibitionData
import com.lzh.tripplan.page.tripdetailpage.datasource.TripDetailDataObserver
import com.lzh.tripplan.page.tripdetailpage.datasource.TripDetailDataSource
import com.lzh.tripplan.utils.EventDetailUtils
import com.lzh.tripplan.viewmodel.BaseViewModel
import com.lzh.tripplan.viewmodel.HandlePageEventResult
import com.lzh.tripplan.viewmodel.IPageHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/6/25
 */
class TripDayScheduleViewModel(val dataSource: TripDetailDataSource, val dayId: Long): BaseViewModel(), TripDetailDataObserver  {
    private val _dayEventList = MutableStateFlow<List<DayEventExhibitionData>>(mutableListOf())
    val dayEventList = _dayEventList.asStateFlow()
    private var daySchedule: DaySchedule? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            dataSource.subscribe(this@TripDayScheduleViewModel)
        }
        daySchedule = dataSource.obtainDaySchedule(dayId)
        val eventListOnOneDay = daySchedule?.dayEventList?.map { it
            createDayEventExhibitionData(it)
        } ?: mutableListOf()
        _dayEventList.value = eventListOnOneDay
    }

    override fun <T : HandlePageEventResult> handlePageEvent(
        pageEvent: PageEvent,
        callback: ((T) -> Unit)?
    ) {

    }

    override suspend fun <T : HandlePageEventResult> syncHandlePageEvent(pageEvent: PageEvent): T? {
        return null
    }

    override fun onTripDetailDataChanged() {
    }

    override fun onCleared() {
        super.onCleared()

        viewModelScope.launch(Dispatchers.IO) {
            dataSource.unsubscribe(this@TripDayScheduleViewModel)
        }
    }

    fun updatePageHandler(pageHandler: IPageHandler?) {
        nextHandler = pageHandler
    }

    private fun createDayEventExhibitionData(dayEvent: DayEvent): DayEventExhibitionData {
        val dayEventId = dayEvent.eventId
        val eventTitle = EventDetailUtils.obtainEventNameDetailContent(dayEvent)
        val eventContent = EventDetailUtils.obtainEventContentDetailContent(dayEvent)
        return DayEventExhibitionData(dayEventId, eventTitle, eventContent)
    }
}