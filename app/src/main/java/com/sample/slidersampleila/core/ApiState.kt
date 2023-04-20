package com.sample.slidersampleila.core

sealed class ApiState {
    object Loading : ApiState()
    class Failure(val errMessage: Throwable) : ApiState()
    class Success(val T: Any) : ApiState()
    object Empty : ApiState()
}