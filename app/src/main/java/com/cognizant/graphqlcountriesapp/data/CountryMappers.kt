package com.cognizant.graphqlcountriesapp.data

import com.cognizant.CountriesQuery
import com.cognizant.CountryQuery
import com.cognizant.graphqlcountriesapp.domain.model.DetailedCountry
import com.cognizant.graphqlcountriesapp.domain.model.SimpleCountry

/**
 * WARNING!!!
 *
 * Do not use meaningful defaults for fields intended for UI use
 */
fun CountryQuery.Country.toDetailedCountry() = DetailedCountry(
    code = this.code,
    name = this.name,
    emoji = this.emoji,
    capital = this.capital ?: "No capital", // Used default for simplicity
    currency = this.currency ?: "No currency", // Used default for simplicity
    languages = this.languages.map { it.name },
    continent = this.continent.name
)

fun CountriesQuery.Country.toSimpleCountry() = SimpleCountry(
    code = this.code,
    name = this.name,
    emoji = this.emoji,
    capital = this.capital ?: "No capital",
)
