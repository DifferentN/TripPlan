package com.lzh.tripplan.viewmodel

import com.lzh.tripplan.event.PageEvent
import com.lzh.tripplan.event.PageEventHandleResponse

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * <out T: HandlePageEventResult>
 * @author zhenghaoli
 * @date 2025/3/10
 */
interface IPageHandler {
    // 可以异步处理，处理结果通过回调返回
    fun <T: HandlePageEventResult> handlePageEvent(pageEvent: PageEvent, callback: ((T) -> Unit)? = null)

    // 同步处理
    suspend fun <T: HandlePageEventResult> syncHandlePageEvent(pageEvent: PageEvent): T?
}
interface HandlePageEventResult

