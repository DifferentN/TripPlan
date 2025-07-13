package com.lzh.tripplan.page.tripdetailpage.event

import com.lzh.tripplan.event.PageEvent

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/7/9
 */
class CreateDayEventEvent(val pageId: String, val dayScheduleId: Long, val dayEventId: Long): PageEvent {
}