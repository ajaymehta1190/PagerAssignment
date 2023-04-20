package com.sample.slidersampleila.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.slidersampleila.core.ApiState
import com.sample.slidersampleila.repository.MasterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel

@Inject
constructor(private val masterRepository: MasterRepository) : ViewModel() {

    private val manufacturerDataListFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Empty)

    val _manufacturerDataListFlow: StateFlow<ApiState> = manufacturerDataListFlow

    fun getManufacturerList() = viewModelScope.launch {
        manufacturerDataListFlow.value = ApiState.Loading
        masterRepository.getMasterData().catch { e ->
            manufacturerDataListFlow.value = ApiState.Failure(e)
        }.collect { data ->
            manufacturerDataListFlow.value = ApiState.Success(data)
        }
    }
}