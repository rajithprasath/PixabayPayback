package com.rajith.pixabaypayback.data.common

import com.rajith.pixabaypayback.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart

/**
 * extension function for Flow Class to emit loading state before the flow starts
 */
fun <T> Flow<Result<T>>.onFlowStarts() =
    onStart {
        emit(Result.Loading)
    }.catch {
        emit(Result.Error(DataSourceException.Unexpected(R.string.error_unexpected_message)))
    }
