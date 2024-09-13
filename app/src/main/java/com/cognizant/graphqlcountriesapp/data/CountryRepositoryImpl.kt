package com.cognizant.graphqlcountriesapp.data

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.cognizant.CountriesQuery
import com.cognizant.CountryQuery
import com.cognizant.graphqlcountriesapp.domain.entity.result.Result
import com.cognizant.graphqlcountriesapp.domain.error.DataError
import com.cognizant.graphqlcountriesapp.domain.model.SimpleCountry
import com.cognizant.graphqlcountriesapp.domain.repository.CountryRepository
import com.cognizant.graphqlcountriesapp.domain.usecase.DetailedCountryResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retryWhen
import java.net.SocketTimeoutException
import java.net.UnknownHostException

private const val MAX_ATTEMPTS = 2
private const val RETRY_DELAY_IN_MS = 10_000L

class CountryRepositoryImpl(
    private val apolloClient: ApolloClient,
    private val dispatcher: CoroutineDispatcher
) : CountryRepository {

    override suspend fun getCountries(): Result<List<SimpleCountry>, DataError.Network> {
        try {
            val response = apolloClient
                .query(CountriesQuery())
                .execute()

            response.run {
                errors?.run {
                    // Apollo specific errors
                    return Result.Error(
                        DataError.Network.ApolloError(
                            this.firstOrNull()?.message ?: ""
                        )
                    )
                }

                return Result.Success(
                    data?.countries?.map { it.toSimpleCountry() } ?: emptyList()
                )
            }
        } catch (exception: Exception) {
            return Result.Error(exception.mapNetworkError())
        }
    }

    override suspend fun getCountry(code: String) = flow<DetailedCountryResult> {
        Log.i("TEST", "Executing getCountry call")
        val response = apolloClient
            .query(CountryQuery(code))
            .execute()

        response.errors?.run {
            // Apollo specific errors
            emit(
                Result.Error(
                    DataError.Network.ApolloError(
                        this.firstOrNull()?.message ?: ""
                    )
                )
            )
        }

        response.data?.country?.run {
            emit(Result.Success(this.toDetailedCountry()))
        } ?: emit(Result.Error(DataError.Network.EmptyResponse("empty response")))
    }.retryWhen { cause, attempt ->
        Log.e("TEST", "retryWhen invoked; attempt: $attempt; cause: $cause")
        if (attempt < MAX_ATTEMPTS && cause is ApolloNetworkException) {
            delay(RETRY_DELAY_IN_MS)
            true
        } else {
            emit(Result.Error(cause.mapNetworkError()))
            false
        }
    }.catch { exception ->
        Log.w("TEST", "catch invoked; exception: ${exception.toString()}")
        emit(Result.Error(exception.mapNetworkError()))
    }.flowOn(dispatcher)
}

fun Throwable.mapNetworkError(): DataError.Network {
    return when (this) {
        is ApolloNetworkException -> {
            when (cause) {
                is SocketTimeoutException,
                is UnknownHostException -> DataError.Network.NoInternet(message)

                else -> DataError.Network.Unknown(message)
            }
        }

        else -> DataError.Network.Unknown(message)
    }
}
