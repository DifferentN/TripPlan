package com.lzh.tripplan.database.entity

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述: 所有旅行计划的集合
 *
 * @author zhenghaoli
 * @date 2025/2/23
 */
data class TripList(
    val tripList: List<Trip>
)
val TRIPLIST_EMPTY = TripList(mutableListOf())