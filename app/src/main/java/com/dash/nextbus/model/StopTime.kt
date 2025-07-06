package com.dash.nextbus.model

data class StopTime(
    val trip_id: String,
    val arrival_time: String,
    val departure_time: String,
    val stop_id: Int,
    val stop_headsign: String
)