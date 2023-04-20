package com.sample.slidersampleila.repository

import com.sample.slidersampleila.core.MasterData
import com.sample.slidersampleila.model.Manufacturers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MasterRepository
@Inject
constructor() {
    fun getMasterData(): Flow<List<Manufacturers>> = flow {
        emit(MasterData.getMasterData())
    }.flowOn(Dispatchers.IO)
}