package com.lzh.tripplan.page.tripdetailpage.eventresult

import com.lzh.tripplan.event.PageEventHandleResponse
import com.lzh.tripplan.viewmodel.HandlePageEventResult

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/3/26
 */
data class CreateDayScheduleResult(val dayScheduleId: Long, val isCreateSuccess: Boolean): HandlePageEventResult