package com.lzh.tripplan.viewmodel.tripdetail

import androidx.lifecycle.viewModelScope
import com.lzh.tripplan.event.PageEvent
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.DayScheduleSummaryData
import com.lzh.tripplan.page.tripdetailpage.datasource.TripDetailDataObserver
import com.lzh.tripplan.page.tripdetailpage.datasource.TripDetailDataSource
import com.lzh.tripplan.viewmodel.BaseViewModel
import com.lzh.tripplan.viewmodel.HandlePageEventResult
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
 * @date 2025/5/14
 */
class TripDetailSummaryViewModel(val dataSource: TripDetailDataSource): BaseViewModel(), TripDetailDataObserver {
    private val _dayScheduleSummaryList = MutableStateFlow<List<DayScheduleSummaryData>>(mutableListOf())
    val dayScheduleSummaryList = _dayScheduleSummaryList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            dataSource.subscribe(this@TripDetailSummaryViewModel)
        }
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
            dataSource.unsubscribe(this@TripDetailSummaryViewModel)
        }
    }
}