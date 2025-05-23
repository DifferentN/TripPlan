package com.lzh.tripplan.page.triplistpage.data.triplist

import com.lzh.tripplan.database.entity.Trip
import com.lzh.tripplan.database.entity.TripList

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/3/3
 */
interface TripListResult {
    class SUCCESS(): TripListResult
    class FAIL() : TripListResult
}