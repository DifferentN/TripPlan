package com.lzh.tripplan.event

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/3/10
 */
interface PageEvent

interface PageEventHandleResponse<T> {
    fun getResult(): T
}
