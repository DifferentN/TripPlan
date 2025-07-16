package com.lzh.tripplan.utils

import com.lzh.tripplan.database.entity.DayEvent
import com.lzh.tripplan.database.entity.DayEventDetail

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/7/16
 */
object EventDetailUtils {
    const val EMPTY_DAY_EVENT_NAME = "Input day event name"
    const val EMPTY_DAY_EVENT_CONTENT = "Input day event content"
    fun sortEventNameDetail(dayEvent: DayEvent?): List<DayEventDetail>? {
        val detailList = mutableListOf<DayEventDetail>()
        dayEvent?.detailList?.let {
            detailList.addAll(it)
        }
        // 根据eventDetailId对detailList 进行排序
        detailList.sortBy { it.eventDetailId }
        return detailList
    }

    fun obtainEventNameDetailId(dayEvent: DayEvent?): Long {
        val detailList = sortEventNameDetail(dayEvent)
        return detailList?.getOrNull(0)?.eventDetailId ?: -1
    }

    fun obtainEventContentDetailId(dayEvent: DayEvent?): Long {
        val detailList = sortEventNameDetail(dayEvent)
        return detailList?.getOrNull(1)?.eventDetailId ?: -1
    }

    fun obtainEventNameDetailContent(dayEvent: DayEvent?): String {
        val detailList = sortEventNameDetail(dayEvent)
        return detailList?.getOrNull(0)?.content ?: EMPTY_DAY_EVENT_NAME
    }

    fun obtainEventContentDetailContent(dayEvent: DayEvent?): String {
        val detailList = sortEventNameDetail(dayEvent)
        return detailList?.getOrNull(1)?.content ?: EMPTY_DAY_EVENT_CONTENT
    }
}