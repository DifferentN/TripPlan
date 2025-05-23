package com.lzh.tripplan.page.tripdetailpage.data.tripdetail

import com.lzh.tripplan.database.entity.Trip
import com.lzh.tripplan.database.entity.TripList
import com.lzh.tripplan.page.triplistpage.data.triplist.TripListResult

/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/3/8
 */
interface TripDetailResult {
    class SUCCESS(): TripDetailResult
    class FAIL() : TripDetailResult
}