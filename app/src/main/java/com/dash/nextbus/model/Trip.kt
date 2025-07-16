package com.dash.nextbus.model

data class Trip(
    val directionId: String?,
    val routeId: String?,
    val tripId: String,
    val tripHeadsign: String?,
    val blockId: String?,
    val shapeId: String?,
    val wheelchairAccessible: String?,
    val bikesAllowed: String?
)
