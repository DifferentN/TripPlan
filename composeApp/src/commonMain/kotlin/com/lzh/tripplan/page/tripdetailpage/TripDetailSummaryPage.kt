package com.lzh.tripplan.page.tripdetailpage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.DayScheduleSummaryData
import com.lzh.tripplan.page.tripdetailpage.datasource.LocalTripDetailDataSources
import com.lzh.tripplan.page.tripdetailpage.datasource.TripDetailDataSource
import com.lzh.tripplan.viewmodel.tripdetail.TripDetailSummaryViewModel

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/5/14
 */
@Composable
fun TripDetailSummaryPage(modifier: Modifier) {
    val dataSource = rememberUpdatedState(LocalTripDetailDataSources.current)
    val viewModel = remember { TripDetailSummaryViewModel(dataSource.value) }
    val daySummaryList = viewModel.dayScheduleSummaryList.collectAsState().value
    Column {
        Text("行程概览")
        for (daySummary in daySummaryList) {
            key(daySummary.dayScheduleId) {
                DaySummaryItem(Modifier, daySummary)
            }
        }
    }
}

@Composable
fun DaySummaryItem(modifier: Modifier, daySummary: DayScheduleSummaryData) {
    val content = StringBuilder()
    daySummary.dayEventDigestList.forEachIndexed { index, item ->
        if (index < daySummary.dayEventDigestList.size - 1) {
            content.append("$item -> ")
        } else {
            content.append(item)
        }
    }
    Column(modifier) {
        Text(daySummary.title)
        Spacer(Modifier.height(6.dp))
        Text(content.toString(), Modifier.height(IntrinsicSize.Min))
    }
}