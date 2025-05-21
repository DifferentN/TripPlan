package com.lzh.tripplan.page.createtrippage.event

import com.lzh.tripplan.event.PageEvent

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/3/22
 */
data class CreateTripNameEvent(val tripName: String,
    val onCreateResult: (Boolean, Long) -> Unit): PageEvent
