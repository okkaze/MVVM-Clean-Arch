package com.cognizant.graphqlcountriesapp.domain.error

sealed interface DataError : Error {
    sealed class Network: DataError {
        abstract val message: String?

        data class NoInternet(override val message: String?): Network()
        data class ApolloError(override val message: String?): Network()
        data class Unknown(override val message: String?): Network()

        data class EmptyResponse(override val message: String?): Network()
    }

    enum class Local: DataError {
        // TODO add Errors such as JSON Validation exceptions..
    }
}
