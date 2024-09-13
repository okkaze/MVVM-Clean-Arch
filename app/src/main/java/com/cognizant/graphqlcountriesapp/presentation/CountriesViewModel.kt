package com.cognizant.graphqlcountriesapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cognizant.graphqlcountriesapp.domain.analytics.Analytics
import com.cognizant.graphqlcountriesapp.domain.analytics.DefaultAnalytics
import com.cognizant.graphqlcountriesapp.domain.entity.result.Result
import com.cognizant.graphqlcountriesapp.domain.error.DataError
import com.cognizant.graphqlcountriesapp.domain.model.DetailedCountry
import com.cognizant.graphqlcountriesapp.domain.model.SimpleCountry
import com.cognizant.graphqlcountriesapp.domain.usecase.GetCountriesUseCase
import com.cognizant.graphqlcountriesapp.domain.usecase.GetCountryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val getCountryUseCase: GetCountryUseCase,
): ViewModel(), Analytics by DefaultAnalytics() {

    private val _state = MutableStateFlow(CountriesState())
    val state: StateFlow<CountriesState> = _state

    data class CountriesState(
        val countries: List<SimpleCountry> = emptyList(),
        val isLoading: Boolean = false,
        val selectedCountry: DetailedCountry? = null,
        val error: String? = null
    )

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            when (val result = getCountriesUseCase()) {
                is Result.Success -> _state.update {
                    it.copy(isLoading = false, countries = result.data)
                }

                is Result.Error -> {
                    val errorMessage = when(result.error) {
                        is DataError.Network.NoInternet -> "Please check your internet connection"
                        is DataError.Network.ApolloError -> "There was an issue when retrieving data"
                        is DataError.Network.Unknown,
                        is DataError.Network.EmptyResponse -> "Unknown error"
                    }
                    _state.update {
                        it.copy(isLoading = false, error = errorMessage)
                    }
                }
            }
        }
        trackScreen("All Countries")
    }

    fun selectCountry(code: String) = viewModelScope.launch {
        trackAction("Select country")
        getCountryUseCase(code).collect { result ->
            when(result) {
                is Result.Success -> _state.update {
                    it.copy(isLoading = false, selectedCountry = result.data)
                }
                is Result.Error -> {
                    val errorMessage = when(result.error) {
                        is DataError.Network.NoInternet -> "Please check your internet connection"
                        is DataError.Network.ApolloError -> "There was an issue when retrieving data"
                        is DataError.Network.Unknown,
                        is DataError.Network.EmptyResponse -> "Unknown error"
                    }
                    _state.update {
                        it.copy(isLoading = false, error = errorMessage)
                    }
                }
            }
        }
    }

    fun dismissCountryDialog() {
        _state.update {
            it.copy(selectedCountry = null)
        }
    }
}
