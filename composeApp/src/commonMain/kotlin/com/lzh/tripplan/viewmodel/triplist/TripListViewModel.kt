package com.lzh.tripplan.viewmodel.triplist

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.lzh.tripplan.database.dao.TripPlanDaoManager
import com.lzh.tripplan.database.entity.TRIPLIST_EMPTY
import com.lzh.tripplan.database.entity.Trip
import com.lzh.tripplan.database.entity.TripList
import com.lzh.tripplan.page.triplistpage.data.triplist.TripListResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/3/2
 */
class TripListViewModel: ViewModel() {
    private val _tripList = MutableStateFlow<List<Trip>>(mutableListOf())
    val tripList = _tripList.asStateFlow()

    suspend fun loadTripList(): TripListResult {
        val trips = mutableListOf<Trip>()
        TripPlanDaoManager.queryAllTripOnlyIdName().forEach {
            trips.add(it)
        }
        _tripList.value = trips
        return TripListResult.SUCCESS()
    }
}