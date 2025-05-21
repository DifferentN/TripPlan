package com.lzh.tripplan.page.triplistpage.transform

import com.lzh.tripplan.data.TripDigestItem
import com.lzh.tripplan.database.entity.Trip

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/3/4
 */
fun Trip.transformToTripDigestItem(): TripDigestItem {
    return TripDigestItem(this.tripId.toInt(), this.tripName ?:"", 0, 0)
}