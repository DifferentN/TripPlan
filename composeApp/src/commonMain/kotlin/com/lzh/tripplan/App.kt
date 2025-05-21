package com.lzh.tripplan

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import com.lzh.tripplan.database.DatabaseManager
import com.lzh.tripplan.page.CREATE_TRIP_PAGE
import com.lzh.tripplan.page.MAIN_PAGE
import com.lzh.tripplan.page.MainPage
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    // 初始化数据库
    DatabaseManager.initDatabaseManager()

    PreComposeApp {
        MaterialTheme {
            val wholeAppNavHostController = rememberNavigator("APP_Navigator")
            NavHost(wholeAppNavHostController, initialRoute = MAIN_PAGE) {
                scene(MAIN_PAGE) {
                    MainPage(wholeAppNavHostController)
                }
                scene(CREATE_TRIP_PAGE) {
                }
            }
        }
    }
}