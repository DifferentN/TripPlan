package com.lzh.tripplan.viewmodel.triplist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lzh.tripplan.database.entity.TripList
import com.lzh.tripplan.page.triplistpage.data.triplist.TripListResult
import kotlinx.coroutines.flow.MutableStateFlow


/**
 * Copyright (c) 2020 Tencent. All rights reserved.
 * 类功能描述:
 *
 * @author zhenghaoli
 * @date 2025/3/2
 */
class TripListViewModel: ViewModel() {
    private val _tripList = MutableStateFlow<TripList>()
    val tripList: State<TripList>
        get() = _tripList

    fun loadTripList(): TripListResult {
        return TripListResult.SUCCESS(tripList.value)
    }
}