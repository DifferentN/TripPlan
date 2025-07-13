package com.lzh.tripplan.page.tripdetailpage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lzh.tripplan.page.CREATE_DAY_EVENT_PAGE
import com.lzh.tripplan.page.TRIP_DETAIL_PAGE_ENTRANCE
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.TripDetailResult
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.TripDetailUiState
import com.lzh.tripplan.view.LoadingView
import com.lzh.tripplan.viewmodel.IPageHandler
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.TripDetailTab
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.TripDetailTabType
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.TripDetailTitleInfo
import com.lzh.tripplan.page.tripdetailpage.datasource.LocalTripDetailDataSources
import com.lzh.tripplan.page.tripdetailpage.datasource.LocalTripDetailViewModelStore
import com.lzh.tripplan.page.tripdetailpage.datasource.TRIP_DETAIL_VIEW_MODEL_STORE
import com.lzh.tripplan.viewmodel.tripdetail.TripDetailViewModel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import org.jetbrains.compose.resources.painterResource
import tripplan.composeapp.generated.resources.Res
import tripplan.composeapp.generated.resources.left_arrow

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 * todo 需要把DaySchedule  DayEvent 拆分成单个Compose UI 并配备ViewModel
 * @author zhenghaoli
 * @date 2025/3/6
 */
@Composable
fun TripDetailPage(tripId: Long, parentPageHandler: IPageHandler, onBack: () -> Unit) {
    val viewModel = remember { TripDetailViewModel() }
    viewModel.updatePageHandler(parentPageHandler)

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
                val detailPageController = rememberNavigator("TripDetailPage")
                viewModel.updateNavigator(detailPageController)
                NavHost(detailPageController, initialRoute = TRIP_DETAIL_PAGE_ENTRANCE) {
                    scene(TRIP_DETAIL_PAGE_ENTRANCE) {
                        TripDetailExhibitionScene(viewModel, onBack)
                    }

                    scene("$CREATE_DAY_EVENT_PAGE/{dayScheduleId}/{dayEventId}") { backStackEntry ->
                        val dayScheduleId: Long = backStackEntry.path<Long>("dayScheduleId") ?: -1
                        val dayEventId: Long = backStackEntry.path<Long>("dayEventId") ?: -1
                        TripDayEventPage(dayScheduleId, dayEventId, viewModel) {
                            detailPageController.goBack()
                        }
                    }
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
fun ColumnScope.TripDetailExhibitionScene(viewModel: TripDetailViewModel, onBack: () -> Unit) {
    CompositionLocalProvider(LocalTripDetailDataSources provides viewModel.dataSource,
        LocalTripDetailViewModelStore provides TRIP_DETAIL_VIEW_MODEL_STORE)
    {
        Box(Modifier.fillMaxSize()) {
            Column {
                TripTitleArea(Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                    TripDetailTitleInfo(viewModel.tripName.collectAsState().value),
                    onBack
                )

                TripContentArea(Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                    viewModel.tripDetailTabList.collectAsState().value,
                    viewModel,
                    createNewDaySchedule = { viewModel.createNewDaySchedule() }
                )
            }
        }
    }
}

@Composable
fun TripTitleArea(modifier: Modifier, titleInfo: TripDetailTitleInfo, onBack: () -> Unit) {
    Box(modifier) {

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
    tripDetailTabList: List<TripDetailTab>,
    viewModel: TripDetailViewModel,
    createNewDaySchedule: () -> Unit
) {
    // 当trip detail 页面销毁时，清除viewModelStore
    val viewModelStore = rememberUpdatedState(LocalTripDetailViewModelStore.current)
    DisposableEffect(true) {
        onDispose {
            viewModelStore.value.clear()
        }
    }
    Box(modifier.border(BorderStroke(2.dp, Color.Gray),
        shape = RoundedCornerShape(6.dp, 6.dp, 0.dp, 0.dp))
    ) {
        Column {
            val navigator = rememberNavigator("tripDetailPageNavigator")
            LazyRow(Modifier.fillMaxWidth()) {
                items(tripDetailTabList, key = {it.tabId}) {
                    TripDetailTab(tab = it,
                        onClick = {
                            navigator.navigate(it.tabId)
                        }
                    )
                }
            }
            // 日程内容
            NavHost(navigator, tripDetailTabList[0].tabId, Modifier.fillMaxWidth().fillMaxHeight()) {
                tripDetailTabList.forEachIndexed{index, tab ->
                    scene(tripDetailTabList[index].tabId){
                        when(tripDetailTabList[index].tabType) {
                            TripDetailTabType.SUMMARY -> TripDetailSummaryPage(Modifier)
                            TripDetailTabType.DAY_TAB -> TripDaySchedulePage(tripDetailTabList[index].dayId, viewModel)
                            else -> AddDayScheduleTab(createNewDaySchedule)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TripDetailTab(tab: TripDetailTab, onClick: () -> Unit) {
    Box(modifier = Modifier
        .padding(4.dp)
        .border(2.dp, color = Color.Gray, shape = RoundedCornerShape(10.dp))
        .background(color = Color.White, shape = RoundedCornerShape(10.dp))
        .clickable { onClick() }
        .padding(4.dp, 2.dp)
    ) {
        Text(tab.tabName, modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun AddDayScheduleTab(createNewDaySchedule: () -> Unit) {
    Text(modifier = Modifier
            .wrapContentSize()
            .clickable { createNewDaySchedule() },
        text = "添加日程")
}
