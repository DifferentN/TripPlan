package com.lzh.tripplan.page.triplistpage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lzh.tripplan.data.TripDigestItem
import com.lzh.tripplan.page.triplistpage.data.triplist.TripListResult
import com.lzh.tripplan.page.triplistpage.data.triplist.TripListUiState
import com.lzh.tripplan.page.triplistpage.transform.transformToTripDigestItem
import com.lzh.tripplan.view.LoadingView
import com.lzh.tripplan.viewmodel.triplist.TripListViewModel
import moe.tlaster.precompose.navigation.Navigator

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/2/8
 */
@Composable
fun TripListPage(modifier: Modifier, wholeAppNavigator: Navigator, onBack: () -> Unit) {
    val viewModel = remember { TripListViewModel() }
    val tripList = remember {
        mutableStateListOf<TripDigestItem>()
    }
    Column(modifier = modifier) {
        Text("Have a nice trip")
        val state by produceState(TripListUiState(isLoading = true)) {
            val result = viewModel.loadTripList()
            if (result is TripListResult.SUCCESS) {
                tripList.clear()
                result.tripList?.tripList?.forEach {
                    tripList.add(it.transformToTripDigestItem())
                }
                value = TripListUiState(isLoading = false, isSuccess = true)
            } else {
                value = TripListUiState(isLoading = false, isSuccess = false)
            }
        }

        when {
            state.isLoading -> {
                Box(Modifier.fillMaxWidth().fillMaxWidth()) {
                    LoadingView(Modifier.width(50.dp).height(50.dp).align(Alignment.Center))
                }
            }
            state.isSuccess -> {
                LazyColumn(Modifier.fillMaxWidth()) {
                    items(tripList,
                        key = {it -> it.tripId}
                    ) {
                        tripAbstractItem(it, wholeAppNavigator)
                    }
                }
            }
            else -> {
                Box(Modifier.fillMaxWidth().fillMaxHeight()) {
                    Text("Loading Error", Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun tripAbstractItem(item: TripDigestItem, navigator: Navigator) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .clickable {

        }
    ) {
        Text(item.title)
        Row {
            Text("启程时间：")
            Text("${item.launchTime}")
        }
        Row {
            Text("结束时间")
            Text("${item.endTime}")
        }

    }
}