package com.example.myapplication.ui.entity

sealed class RequestState {
    object Idle: RequestState()
    object Processing: RequestState()
    data class Success<T>(val data: T): RequestState()
    data class Error(val message: String): RequestState()
}