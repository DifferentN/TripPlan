package com.lzh.tripplan.page.tripdetailpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lzh.tripplan.page.tripdetailpage.data.tripdetail.TripDetailTitleInfo
import com.lzh.tripplan.page.tripdetailpage.datasource.LocalTripDetailDataSources
import com.lzh.tripplan.viewmodel.IPageHandler
import com.lzh.tripplan.viewmodel.tripdetail.TripDayEventViewModel
import org.jetbrains.compose.resources.painterResource
import tripplan.composeapp.generated.resources.Res
import tripplan.composeapp.generated.resources.left_arrow

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/7/9
 */
@Composable
fun TripDayEventPage(dayScheduleId: Long, dayEventId: Long, parentPageHandler: IPageHandler, onBack: () -> Unit) {
    val dataSource = rememberUpdatedState(LocalTripDetailDataSources.current)
    val viewModel = remember { TripDayEventViewModel(dayScheduleId, dayEventId, dataSource.value) }

    Column {
        DayEventTitleArea(
            Modifier.fillMaxWidth().height(30.dp),
            onBack = {
                onBack()
            },
            onSave = {
                viewModel.saveDayEvent()
            }
        )
        DayEventContentArea(
            Modifier.fillMaxWidth().fillMaxHeight(),
            viewModel.dayEventName.value,
            viewModel.dayEventContent.value,
            onUpdateEventName = { it -> viewModel.updateDayEventName (it) },
            onUpdateEventContent = {it -> viewModel.updateDayEventContent(it)}
        )
    }
}

@Composable
fun DayEventTitleArea(modifier: Modifier, onBack: () -> Unit, onSave: () -> Unit) {
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
            .align(Alignment.TopEnd)
        ) {
            Text(text = "保存",
                modifier = Modifier.clickable { onSave() }
            )
        }
    }
}

@Composable
fun DayEventContentArea(modifier: Modifier,
    eventName: String,
    eventContent: String,
    onUpdateEventName: (name: String) -> Unit,
    onUpdateEventContent: (name: String) -> Unit) {
    Box(modifier = modifier) {
        Column {
            Text("日程标题")
            var dayEventName by remember { mutableStateOf(eventName) }
            var dayEventContent by remember { mutableStateOf(eventContent) }
            TextField(dayEventName,
                placeholder = { Text(dayEventName) },
                onValueChange = {
                    dayEventName = it
                    onUpdateEventName(dayEventName)
                },
                textStyle = TextStyle(fontSize = 30.sp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            Spacer(Modifier.height(6.dp))

            Text("日程内容")
            TextField(dayEventContent,
                placeholder = { Text(dayEventContent) },
                onValueChange = {
                    dayEventContent = it
                    onUpdateEventContent(dayEventContent)
                },
                textStyle = TextStyle(fontSize = 30.sp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }
    }
}