package com.cognizant.graphqlcountriesapp.di

import com.apollographql.apollo3.ApolloClient
import com.cognizant.graphqlcountriesapp.data.CountryRepositoryImpl
import com.cognizant.graphqlcountriesapp.domain.repository.CountryRepository
import com.cognizant.graphqlcountriesapp.domain.usecase.GetCountriesUseCase
import com.cognizant.graphqlcountriesapp.domain.usecase.GetCountryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("https://countries.trevorblades.com/graphql")
            .build()
    }

    @Provides
    @Singleton
    fun provideCountryClient(apolloClient: ApolloClient): CountryRepository {
        return CountryRepositoryImpl(apolloClient, Dispatchers.IO)
    }

    @Provides
    @Singleton
    fun provideCountriesUseCase(countryRepository: CountryRepository): GetCountriesUseCase {
        return GetCountriesUseCase(countryRepository)
    }

    @Provides
    @Singleton
    fun provideCountryUseCase(countryRepository: CountryRepository): GetCountryUseCase {
        return GetCountryUseCase(countryRepository)
    }


}