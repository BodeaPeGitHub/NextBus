package com.dash.nextbus.model

data class Agency(
    val agencyId: Int,
    val agencyName: String,
    val agencyTimezone: String,
    val agencyLang: String,
    val agencyUrl: String,
    val agencyUrls: List<String>
)