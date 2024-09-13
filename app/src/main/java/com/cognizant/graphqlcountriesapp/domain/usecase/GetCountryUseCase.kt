package com.cognizant.graphqlcountriesapp.domain.usecase

import com.cognizant.graphqlcountriesapp.domain.entity.result.Result
import com.cognizant.graphqlcountriesapp.domain.error.DataError
import com.cognizant.graphqlcountriesapp.domain.model.DetailedCountry
import com.cognizant.graphqlcountriesapp.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

typealias DetailedCountryResult = Result<DetailedCountry?, DataError.Network>

class GetCountryUseCase(
    private val countryRepository: CountryRepository
) {
    suspend operator fun invoke(code: String): Flow<DetailedCountryResult> {
        return countryRepository.getCountry(code)
    }
}
