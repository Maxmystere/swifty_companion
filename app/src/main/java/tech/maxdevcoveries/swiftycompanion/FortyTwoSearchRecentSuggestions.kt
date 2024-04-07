package tech.maxdevcoveries.swiftycompanion

import android.content.SearchRecentSuggestionsProvider

class FortyTwoSearchRecentSuggestions : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "tech.maxdevcoveries.swiftycompanion.FortyTwoSearchRecentSuggestions"
        const val MODE: Int = DATABASE_MODE_QUERIES
    }
}