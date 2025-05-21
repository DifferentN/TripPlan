package com.lzh.tripplan.page.tripdetailpage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lzh.tripplan.database.entity.DEFAULT_DAY_EVENT_DETAIL_ID
import com.lzh.tripplan.database.entity.DEFAULT_DAY_EVENT_ID
import com.lzh.tripplan.database.entity.DayEvent
import com.lzh.tripplan.database.entity.DayEventDetail
import com.lzh.tripplan.database.entity.DaySchedule
import com.lzh.tripplan.database.entity.Trip
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.AddDayScheduleItem
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.DayScheduleTab
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.DetailPageDayContent
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.DetailPageDayContentType
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.TripDetailResult
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.TripDetailUiState
import com.lzh.tripplan.view.LoadingView
import com.lzh.tripplan.viewmodel.IPageHandler
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.ADD_TAB_TYPE_ID
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.ADD_TAB_TYPE_NAME
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.DetailPageDayTabType
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.TripDetailTab
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.TripDetailTabType
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.TripDetailTitleInfo
import com.lzh.tripplan.page.tripdetailpage.datasource.LocalTripDetailDataSources
import com.lzh.tripplan.page.tripdetailpage.datasource.TripDetailDataSource
import com.lzh.tripplan.page.tripdetailpage.event.AddUpdateEventDetail
import com.lzh.tripplan.page.tripdetailpage.event.AddScheduleEvent
import com.lzh.tripplan.page.tripdetailpage.event.EditTripEvent
import com.lzh.tripplan.page.tripdetailpage.event.CreateDayScheduleEvent
import com.lzh.tripplan.page.tripdetailpage.event.SaveTripEvent
import com.lzh.tripplan.page.tripdetailpage.eventresult.CreateDayScheduleResult
import com.lzh.tripplan.viewmodel.HandlePageEventResult
import com.lzh.tripplan.viewmodel.tripdetail.TripDetailViewModel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import org.jetbrains.compose.resources.painterResource
import tripplan.composeapp.generated.resources.Res
import tripplan.composeapp.generated.resources.edit
import tripplan.composeapp.generated.resources.left_arrow
import tripplan.composeapp.generated.resources.tick

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 * todo 需要把DaySchedule  DayEvent 拆分成单个Compose UI 并配备ViewModel
 * @author zhenghaoli
 * @date 2025/3/6
 */
@Composable
fun TripDetailPage(tripId: Long, onBack: () -> Unit) {
    val viewModel = remember { TripDetailViewModel() }

    Column {
        val tripState by produceState(TripDetailUiState(true, false)) {
            val result = viewModel.loadTrip(tripId)
            if (result is TripDetailResult.SUCCESS) {
                value = TripDetailUiState(false, true)
            } else {
                value = TripDetailUiState(false, false)
            }
        }

        when {
            tripState.isLoading -> {
                Box(Modifier.fillMaxWidth().fillMaxWidth()) {
                    LoadingView(Modifier.width(50.dp).height(50.dp).align(Alignment.Center))
                }
            }
            tripState.isSuccess -> {
                CompositionLocalProvider(LocalTripDetailDataSources provides viewModel.dataSource) {
                    TripTitleArea(Modifier
                        .height(60.dp)
                        .fillMaxWidth(),
                        TripDetailTitleInfo(viewModel.tripName.collectAsState().value),
                        onBack
                    )

                    TripContentArea(Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                        viewModel.tripDetailTabList.collectAsState().value
                    )
                }
            }
            else -> {
                Box(Modifier.fillMaxWidth().fillMaxHeight()) {
                    Text("Loading Error")
                }
            }
        }
    }
}

@Composable
fun TripTitleArea(modifier: Modifier, titleInfo: TripDetailTitleInfo, onBack: () -> Unit) {
    Box(Modifier.fillMaxWidth().height(60.dp)) {

        Image(modifier = Modifier
            .width(20.dp)
            .height(20.dp)
            .align(Alignment.CenterStart)
            .clickable {
                onBack()
            },
            painter = painterResource(Res.drawable.left_arrow),
            contentDescription = "")

        Box(Modifier.wrapContentSize()
            .align(Alignment.Center)
        ) {
            Text(text = titleInfo.tripName)
        }
    }
}

@Composable
fun ColumnScope.TripContentArea(modifier: Modifier,
    tripDetailTabList: List<TripDetailTab>
) {
    Box(modifier.border(BorderStroke(2.dp, Color.Gray),
        shape = RoundedCornerShape(6.dp, 6.dp, 0.dp, 0.dp))
    ) {
        Column {
            val navigator = rememberNavigator("tripDetailPageNavigator")
            LazyRow(Modifier.fillMaxWidth()) {
                items(tripDetailTabList, key = {it.tabId}) {
                    Text()
                }
            }
            // 日程内容
            NavHost(navigator, tripDetailTabList[0].tabId, Modifier.fillMaxWidth().fillMaxHeight()) {
                tripDetailTabList.forEachIndexed{index, tab ->
                    scene(tripDetailTabList[index].tabId){
                        when(tripDetailTabList[index].tabType) {
                            TripDetailTabType.SUMMARY -> TripDetailSummaryPage(Modifier)
                            TripDetailTabType.DAY_TAB -> Text("日程")
                            else -> Text("添加")
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun TripDetailDaySchedule(item: DaySchedule?, pageState: TripDetailPageState, pageHandler: IPageHandler) {
    if (item == null) {
        Text("Occur Error")
        return
    }
    val eventList = mutableListOf<DayEvent>()

    if (!item.dayEventList.isNullOrEmpty()) {
        eventList.addAll(item.dayEventList!!)
    }
    LazyColumn(Modifier.fillMaxWidth().fillMaxHeight()) {
        items(eventList, key = {it -> it.hashCode()}) { dayEvent ->
            TripDayEvent(dayEvent, pageState, pageHandler)
        }
        if (pageState == TripDetailPageState.EDIT_STATE) {
            item {
                TripDetailAddEvent(pageHandler, item.scheduleId)
            }
        }
    }
}

@Composable
fun TripDayEvent(dayEvent: DayEvent, pageState: TripDetailPageState, pageHandler: IPageHandler) {
    val eventDetailList = mutableListOf<DayEventDetail>()
    if (!dayEvent.detailList.isNullOrEmpty()) {
        eventDetailList.addAll(dayEvent.detailList!!)
    }
    LazyRow(Modifier.fillMaxWidth().height(60.dp)) {
        items(eventDetailList, key = { it -> it.hashCode()}) { eventDetail ->
            var detailContent by remember { mutableStateOf(eventDetail.content) }
            if (pageState == TripDetailPageState.EDIT_STATE) {
                TextField(detailContent,
                    onValueChange = {
                        detailContent = it
                        // 保存 Event Detail内容
                        pageHandler.handlePageEvent<HandlePageEventResult>(AddUpdateEventDetail(eventDetail.eventDetailId,
                            DEFAULT_DAY_EVENT_DETAIL_ID, detailContent))
                    },
                    textStyle = TextStyle(fontSize = 30.sp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
            } else {
                Text(eventDetail.content)
            }
        }
        if (pageState == TripDetailPageState.EDIT_STATE) {
            item {
                TripDetailAddEventDetail(pageHandler, dayEvent.eventId)
            }
        }
    }
}

@Composable
fun BoxScope.TripDetailAddDaySchedule(pageHandler: IPageHandler, isShowCreateDayScheduleTab: MutableState<Boolean>) {
    if (!isShowCreateDayScheduleTab.value) {
        return
    }
    // 是否展示创建的DaySchedule的动画
    var isShowCreateDayScheduleLoading by mutableStateOf(false)
    var dayScheduleName by remember { mutableStateOf("") }
    Column(Modifier.background(Color.Gray)
        .wrapContentHeight()
        .width(240.dp)
        .align(Alignment.Center)
    ) {
        Text("创建新的日程")
        Spacer(Modifier.height(30.dp))
        TextField(dayScheduleName,
            placeholder = { Text("输入旅程名称") },
            onValueChange = {
                dayScheduleName = it
            },
            textStyle = TextStyle(fontSize = 30.sp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Text("confirm",
                Modifier.clickable {
                    isShowCreateDayScheduleLoading = true
                    pageHandler.handlePageEvent(CreateDayScheduleEvent(dayScheduleName)) {result: CreateDayScheduleResult ->
                        isShowCreateDayScheduleLoading = false
                        isShowCreateDayScheduleTab.value = false
                    }
                },
                fontSize = 12.sp)

            Text("cancel",
                Modifier.clickable {
                    isShowCreateDayScheduleTab.value = false
                },
                fontSize = 12.sp)
        }
    }
}

@Composable
fun TripDetailAddEvent(pageHandler: IPageHandler, dayScheduleId: Long) {
    Text("Add Event",
        Modifier.height(60.dp).fillMaxWidth().clickable {
            pageHandler.handlePageEvent<HandlePageEventResult>(AddScheduleEvent(dayScheduleId))
        }
    )
}

@Composable
fun TripDetailAddEventDetail(pageHandler: IPageHandler, dayEventId: Long) {
    Text("Add EventDetail",
        Modifier.height(60.dp).width(20.dp).clickable {
            pageHandler.handlePageEvent<HandlePageEventResult>(AddUpdateEventDetail(dayEventId, DEFAULT_DAY_EVENT_DETAIL_ID, ""))
        })
}
