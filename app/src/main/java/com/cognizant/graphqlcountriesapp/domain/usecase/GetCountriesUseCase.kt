package com.cognizant.graphqlcountriesapp.domain.usecase

import com.cognizant.graphqlcountriesapp.domain.entity.result.Result
import com.cognizant.graphqlcountriesapp.domain.error.DataError
import com.cognizant.graphqlcountriesapp.domain.model.SimpleCountry
import com.cognizant.graphqlcountriesapp.domain.repository.CountryRepository

class GetCountriesUseCase(
    // Inject analytics
    private val countryRepository: CountryRepository
) {
    suspend operator fun invoke(): Result<List<SimpleCountry>, DataError.Network> {
        return countryRepository.getCountries()
    }
}
