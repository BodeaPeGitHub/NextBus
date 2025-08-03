package com.dash.nextbus.model;

data class Vehicle(
    val id:Int,
    val label:String?,
    val latitude:Double?,
    val longitude:Double?,
    val timestamp:String?,
    val speed:Int?,
    val routeId:Int?,
    val tripId:Int?,
    val vehicleType:Int?,
    val bikeAccessible:String?,
    val wheelchairAccessible:String?
)