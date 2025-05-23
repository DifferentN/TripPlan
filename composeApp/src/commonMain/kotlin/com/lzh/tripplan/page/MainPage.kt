package com.lzh.tripplan.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lzh.tripplan.page.createtrippage.CreateTripPage
import com.lzh.tripplan.page.tripdetailpage.TripDetailPage
import com.lzh.tripplan.page.triplistpage.TripListPage
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/3/2
 */
@Composable
fun MainPage(wholeAppNavigator: Navigator) {
    val mainPageNavigator = rememberNavigator()
    Scaffold(
        Modifier.fillMaxWidth().fillMaxHeight(),
        bottomBar = {
            Row(Modifier
                .background(color = Color.Gray)
                .padding(0.dp, 4.dp)
                .height(20.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text("Trips",
                    modifier = Modifier.clickable {
                        mainPageNavigator.navigate(TRIP_LIST_PAGE, options = NavOptions(launchSingleTop = true))
                    }
                )
                Text("create",
                    Modifier.clickable {
                        mainPageNavigator.navigate(CREATE_TRIP_PAGE, options = NavOptions(launchSingleTop = true))
                    }
                )
                Text("Mine",
                    Modifier.clickable {
                        mainPageNavigator.navigate(MINE_PAGE, options = NavOptions(launchSingleTop = true))
                    }
                )
            }
        }
    ) {

        NavHost(mainPageNavigator, initialRoute = TRIP_LIST_PAGE) {
            scene(TRIP_LIST_PAGE) {
                TripListPage(Modifier,
                    onClickTrip = {tripId ->
                        mainPageNavigator.navigate("$EXHIBITION_TRIP_PAGE/$tripId",
                            options = NavOptions(popUpTo = PopUpTo(TRIP_LIST_PAGE, false))
                        )
                    },
                    onBack = {
                        wholeAppNavigator.goBack()
                    }
                )
            }
            scene(CREATE_TRIP_PAGE) {
                Text("Add Trip")
                CreateTripPage(Modifier, onBack = {mainPageNavigator.goBack()}) { isSuccess, tripId ->
                    if (isSuccess) {
                        mainPageNavigator.navigate("$EXHIBITION_TRIP_PAGE/$tripId",
                            options = NavOptions(popUpTo = PopUpTo(TRIP_LIST_PAGE, false))
                        )
                    } else {
                        mainPageNavigator.goBack()
                    }
                }
            }
            scene(MINE_PAGE) {
                Text("Mine Page")
            }
            scene("$EXHIBITION_TRIP_PAGE/{id}") {backStackEntry ->
                val id: Long = backStackEntry.path<Long>("id") ?: -1
                TripDetailPage(id.toLong()) {
                    wholeAppNavigator.goBack()
                }
            }
        }
    }

}