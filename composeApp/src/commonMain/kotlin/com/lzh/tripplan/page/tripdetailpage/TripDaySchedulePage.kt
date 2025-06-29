package com.lzh.tripplan.page.tripdetailpage

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.DayEventExhibitionData
import com.lzh.tripplan.page.tripdetailpage.datasource.LocalTripDetailDataSources
import com.lzh.tripplan.page.tripdetailpage.datasource.LocalTripDetailViewModelStore
import com.lzh.tripplan.viewmodel.tripdetail.TripDayScheduleViewModel

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/6/24
 */
@Composable
fun TripDaySchedulePage(dayId: Long) {
    val dataSource = rememberUpdatedState(LocalTripDetailDataSources.current)
    val viewModelStore = rememberUpdatedState(LocalTripDetailViewModelStore.current)
    // 根据dayId创建一个ViewModel，然后把此ViewModel存储到viewModelStore中
    var vm: TripDayScheduleViewModel? = viewModelStore.value.get("$dayId") as TripDayScheduleViewModel?
    if (vm == null) {
        vm = TripDayScheduleViewModel(dataSource.value, dayId)
        viewModelStore.value.put("$dayId", vm)
    }
    val viewModel by rememberUpdatedState(vm)
    val eventList = viewModel.dayEventList.collectAsState().value
    LazyColumn {
        items(eventList, key = { it.eventId }) {
            TripDayEvent(it)
        }
    }
}

@Composable
fun TripDayEvent(dayEventExhibitionData: DayEventExhibitionData) {
    Column(Modifier.border(2.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
        .padding(0.dp, 4.dp))
    {
        Text(dayEventExhibitionData.title)
        Spacer(Modifier.height(12.dp))
        Text(dayEventExhibitionData.comment)
    }
}
