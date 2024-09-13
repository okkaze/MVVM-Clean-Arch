package com.cognizant.graphqlcountriesapp.domain.repository

import com.cognizant.graphqlcountriesapp.domain.entity.result.Result
import com.cognizant.graphqlcountriesapp.domain.error.DataError
import com.cognizant.graphqlcountriesapp.domain.model.SimpleCountry
import com.cognizant.graphqlcountriesapp.domain.usecase.DetailedCountryResult
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    suspend fun getCountries(): Result<List<SimpleCountry>, DataError.Network>
    suspend fun getCountry(code: String): Flow<DetailedCountryResult>
}
