package com.dash.nextbus.model

data class Agency(
    val agency_id: Int,
    val agency_name: String,
    val agency_timezone: String,
    val agency_lang: String,
    val agency_url: String,
    val agency_urls: List<String>
)