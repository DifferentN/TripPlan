package com.lzh.tripplan.viewmodel.tripdetail

import com.lzh.tripplan.event.PageEvent
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.DayScheduleSummaryData
import com.lzh.tripplan.page.tripdetailpage.datasource.TripDetailDataSource
import com.lzh.tripplan.viewmodel.BaseViewModel
import com.lzh.tripplan.viewmodel.HandlePageEventResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/5/14
 */
class TripDetailSummaryViewModel(val dataSource: TripDetailDataSource): BaseViewModel(){
    private val _dayScheduleSummaryList = MutableStateFlow<List<DayScheduleSummaryData>>(mutableListOf())
    val dayScheduleSummaryList = _dayScheduleSummaryList.asStateFlow()

    override fun <T : HandlePageEventResult> handlePageEvent(
        pageEvent: PageEvent,
        callback: ((T) -> Unit)?
    ) {

    }

    override suspend fun <T : HandlePageEventResult> syncHandlePageEvent(pageEvent: PageEvent): T? {
        return null
    }

}