package com.cognizant.graphqlcountriesapp.domain.analytics

import android.util.Log

class DefaultAnalytics : Analytics {
    override fun trackScreen(screenName: String) {
        Log.w("ANALYTICS", "Opened screen: $screenName")
    }

    override fun trackAction(actionName: String) {
        Log.w("ANALYTICS", "Performed action: $actionName")
    }
}
