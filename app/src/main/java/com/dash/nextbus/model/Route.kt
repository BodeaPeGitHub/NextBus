package com.dash.nextbus.model

data class Route(
    val routeId: String,
    val agencyId: Int?,
    val routeShortName: String?,
    val routeLongName: String?,
    val routeColor: String?,
    val routeType: Int?,
    val routeDesc: String?
)