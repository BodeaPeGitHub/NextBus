package com.dash.nextbus.model

data class Stop(
    val stopId: Int,
    val stopName: String,
    val stopDesc: String?,
    val stopLat: Double,
    val stopLon: Double,
    val locationType: Int,
    val stopCode: String?
)