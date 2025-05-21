package com.lzh.tripplan.page.tripdetailpage.event

import com.lzh.tripplan.event.PageEvent

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/3/30
 */
data class AddUpdateEventDetail(val eventId: Long, val priority: Long, val content: String): PageEvent