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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lzh.tripplan.data.TripDigestItem
import com.lzh.tripplan.database.entity.Trip
import com.lzh.tripplan.page.EXHIBITION_TRIP_PAGE
import com.lzh.tripplan.page.TRIP_LIST_PAGE
import com.lzh.tripplan.page.triplistpage.data.triplist.TripListResult
import com.lzh.tripplan.page.triplistpage.data.triplist.TripListUiState
import com.lzh.tripplan.page.triplistpage.transform.transformToTripDigestItem
import com.lzh.tripplan.view.LoadingView
import com.lzh.tripplan.viewmodel.triplist.TripListViewModel
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/2/8
 */
@Composable
fun TripListPage(modifier: Modifier, onClickTrip: (tripId: Long) -> Unit) {
    val viewModel = remember { TripListViewModel() }
    Column(modifier = modifier) {
        Text("Have a nice trip")
        val state by produceState(TripListUiState(isLoading = true)) {
            val result = viewModel.loadTripList()
            if (result is TripListResult.SUCCESS) {
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
                val tripList = viewModel.tripList.collectAsState().value
                LazyColumn(Modifier.fillMaxWidth()) {
                    items(tripList,
                        key = {it -> it.tripId}
                    ) {
                        tripAbstractItem(it, onClickTrip)
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
fun tripAbstractItem(item: Trip, onClickTrip: (tripId: Long) -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .clickable {
            onClickTrip(item.tripId)
        }
    ) {
        Text(item.tripName.toString())
    }
}