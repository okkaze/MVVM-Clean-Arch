package com.cognizant.graphqlcountriesapp.domain.analytics

interface Analytics {
    fun trackScreen(screenName: String)
    fun trackAction(actionName: String)
}
