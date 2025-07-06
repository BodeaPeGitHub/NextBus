package com.dash.nextbus.model

data class StopTime(
    val tripId: String,
    val arrivalTime: String,
    val departureTime: String,
    val stopId: Int,
    val stopHeadsign: String
)