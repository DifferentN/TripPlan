package com.lzh.tripplan.viewmodel

import androidx.compose.ui.node.WeakReference
import com.lzh.tripplan.event.PageEvent
import com.lzh.tripplan.page.tripdetailpage.event.CreateDayEventEvent
import moe.tlaster.precompose.navigation.Navigator

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/7/9
 */
class AppViewModel: BaseViewModel() {

    override fun <T : HandlePageEventResult> handlePageEvent(
        pageEvent: PageEvent,
        callback: ((T) -> Unit)?
    ) {

    }

    override suspend fun <T : HandlePageEventResult> syncHandlePageEvent(pageEvent: PageEvent): T? {
        return null
    }


}