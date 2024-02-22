package com.droidcon.forecaster

class QueryLengthValidator(
    private val requiredLength: Int = 3
) {

    fun isValidQuery(location: String): Boolean {
        return location.trim().length >= requiredLength
    }
}