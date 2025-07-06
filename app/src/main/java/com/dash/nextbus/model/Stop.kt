package com.dash.nextbus.model

data class Stop(
    val stop_id: Int,
    val stop_name: String,
    val stop_desc: String?,
    val stop_lat: Double,
    val stop_lon: Double,
    val location_type: Int,
    val stop_code: String?
)