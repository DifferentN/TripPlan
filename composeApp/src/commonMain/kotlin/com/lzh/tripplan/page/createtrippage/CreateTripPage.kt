package com.lzh.tripplan.page.createtrippage

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lzh.tripplan.page.createtrippage.event.CreateTripNameEvent
import com.lzh.tripplan.view.LoadingView
import com.lzh.tripplan.viewmodel.createtrippage.CreateTripPageViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import tripplan.composeapp.generated.resources.Res
import tripplan.composeapp.generated.resources.left_arrow

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/3/22
 */
@Composable
fun CreateTripPage(modifier: Modifier,
    onBack: () -> Unit,
    onCreateResult: (isSuccess: Boolean, tripId: Long) -> Unit)
{
    val viewModel = remember { CreateTripPageViewModel() }
    var tripName by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val scope = rememberCoroutineScope()
        var isShowCreateTripLoading = mutableStateOf(false)
        // 顶部导航
        Row(modifier = Modifier.height(50.dp).fillMaxWidth()) {
            Image(modifier = Modifier
                .width(20.dp)
                .height(20.dp)
                .clickable {
                    onBack()
                },
                painter = painterResource(Res.drawable.left_arrow),
                contentDescription = "")
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("创建旅程")
            }
        }
        Box(Modifier.fillMaxWidth().fillMaxHeight()) {

            Column(Modifier.fillMaxWidth().fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                // 旅程名称输入
                TextField(tripName,
                    modifier = Modifier
                        .padding(8.dp, 0.dp)
                        .fillMaxWidth()
                        .align(Alignment.Start)
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(4.dp)),
                    placeholder = { Text("输入旅程名称") },
                    onValueChange = {
                        tripName = it
                    },
                    textStyle = TextStyle(fontSize = 30.sp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    modifier = Modifier
                        .align(Alignment.End)
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(4.dp))
                        .padding(8.dp, 0.dp)
                        .clickable {
                            scope.launch {
                                isShowCreateTripLoading.value = true
                                val result = viewModel.createTrip(tripName)
                                isShowCreateTripLoading.value = false
                                onCreateResult(result.isSuccess, result.tripId)
                            } },
                    text = "确认",
                    fontSize = 18.sp)
            }

            if (isShowCreateTripLoading.value) {
                LoadingView(Modifier.width(50.dp).height(50.dp).align(Alignment.Center))
            }
        }

    }
}