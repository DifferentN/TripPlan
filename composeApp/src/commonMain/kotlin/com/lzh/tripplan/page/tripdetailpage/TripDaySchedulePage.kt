package com.lzh.tripplan.page.tripdetailpage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lzh.tripplan.page.CREATE_DAY_EVENT_PAGE
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.DayEventExhibitionData
import com.lzh.tripplan.page.tripdetailpage.datasource.LocalTripDetailDataSources
import com.lzh.tripplan.page.tripdetailpage.datasource.LocalTripDetailViewModelStore
import com.lzh.tripplan.page.tripdetailpage.event.CreateModifyDayEventEvent
import com.lzh.tripplan.utils.DAY_SCHEDULE_DETAIL_BG
import com.lzh.tripplan.viewmodel.HandlePageEventResult
import com.lzh.tripplan.viewmodel.IPageHandler
import com.lzh.tripplan.viewmodel.tripdetail.TripDayScheduleViewModel

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/6/24
 */
@Composable
fun TripDaySchedulePage(dayId: Long, parentPageHandler: IPageHandler?) {
    val scope = rememberCoroutineScope()
    val dataSource = rememberUpdatedState(LocalTripDetailDataSources.current)
    val viewModelStore = rememberUpdatedState(LocalTripDetailViewModelStore.current)
    // 根据dayId创建一个ViewModel，然后把此ViewModel存储到viewModelStore中
    var vm: TripDayScheduleViewModel? = viewModelStore.value.get("$dayId") as TripDayScheduleViewModel?
    if (vm == null) {
        vm = TripDayScheduleViewModel(dataSource.value, dayId)
        viewModelStore.value.put("$dayId", vm)
    }
    vm.updatePageHandler(parentPageHandler)

    val viewModel by rememberUpdatedState(vm)
    val eventList = viewModel.dayEventList.collectAsState().value
    Box(Modifier.fillMaxSize().padding(4.dp, 4.dp)) {
        LazyColumn(Modifier.fillMaxSize().align(Alignment.Center)) {
            items(eventList, key = { it.eventId }) {
                TripDayEvent(
                    it,
                    onDayEventClick = {dayEventId ->
                        parentPageHandler?.handlePageEvent(
                            CreateModifyDayEventEvent(CREATE_DAY_EVENT_PAGE, dayId, dayEventId)
                        ) { result: HandlePageEventResult ->
                            // no nothing
                        }
                    }
                )
                Spacer(Modifier.height(4.dp))
            }
            item(key = "addDayEvent") {
                AddDayEvent(Modifier.fillMaxWidth(),
                    onClickAddDayEvent = {
                        parentPageHandler?.handlePageEvent(
                            CreateModifyDayEventEvent(CREATE_DAY_EVENT_PAGE, dayId, -1)
                        ) { result: HandlePageEventResult ->
                            // no nothing
                        }
                    }
                )
            }
        }
    }

}

@Composable
fun TripDayEvent(dayEventExhibitionData: DayEventExhibitionData, onDayEventClick: (dayEventId: Long) -> Unit) {
    Column(Modifier.background(DAY_SCHEDULE_DETAIL_BG, shape = RoundedCornerShape(6.dp))
        .fillMaxWidth()
        .padding(2.dp, 4.dp)
        .clickable { onDayEventClick(dayEventExhibitionData.eventId) }
    ) {
        Text(dayEventExhibitionData.title)
        Spacer(Modifier.height(6.dp))
        Text(dayEventExhibitionData.comment)
    }
}

@Composable
fun AddDayEvent(modifier: Modifier, onClickAddDayEvent: () -> Unit) {
    Box(modifier = modifier) {
        Text(text = "添加活动",
            modifier = Modifier.fillMaxWidth()
                .wrapContentHeight().clickable {
                    onClickAddDayEvent()
                }
        )
    }
}

@Composable
fun BottomAddDayEventBox(
    onCloseBox: () -> Unit,
    onConfirmAddEvent: (content: String) -> Unit
) {
    var eventComment by remember { mutableStateOf("") }

    Box(Modifier.fillMaxSize().background(Color(Color.Gray.red, Color.Gray.green, Color.Gray.blue, 0.3f))) {
        Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            TextField(eventComment,
                placeholder = { Text("输入活动名称") },
                onValueChange = {
                    eventComment = it
                },
                textStyle = TextStyle(fontSize = 30.sp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
            Text("确认",
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { onConfirmAddEvent(eventComment) }
            )
        }
    }

}
