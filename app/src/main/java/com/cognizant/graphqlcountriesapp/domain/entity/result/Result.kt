package com.cognizant.graphqlcountriesapp.domain.entity.result

import com.cognizant.graphqlcountriesapp.domain.error.Error

typealias AppError = Error

sealed interface Result<out D, out E: AppError> {
    data class Success<out D>(val data: D): Result<D, Nothing>
    data class Error<out E: AppError>(val error: E): Result<Nothing, E>
}
