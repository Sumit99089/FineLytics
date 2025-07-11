package com.example.finelytics.utils

sealed class Resource<T>(
    val data: T? = null,
    val errorMessage: String? = null
) {
    class Success<T>(data: T?): Resource<T>(data = data, errorMessage = null)

    class Error<T>(message: String, data: T? = null): Resource<T>(data = data,errorMessage = message)

    class Loading<T>(val isLoading: Boolean = true): Resource<T>(data = null, errorMessage = null)
}