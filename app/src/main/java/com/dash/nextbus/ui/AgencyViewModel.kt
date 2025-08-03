package com.dash.nextbus.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dash.nextbus.model.Agency
import com.dash.nextbus.model.Route
import com.dash.nextbus.model.Stop
import com.dash.nextbus.model.StopTime
import com.dash.nextbus.model.Trip
import com.dash.nextbus.model.Vehicle
import com.dash.nextbus.service.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AgencyViewModel : ViewModel() {

    private val _agencies = MutableStateFlow<List<Agency>>(emptyList())
    val agencies: StateFlow<List<Agency>> = _agencies

    private val _stops = MutableStateFlow<List<Stop>>(emptyList())
    val stops: StateFlow<List<Stop>> = _stops

    private val _stopTimes = MutableStateFlow<List<StopTime>>(emptyList())
    val stopTimes: StateFlow<List<StopTime>> = _stopTimes

    private val _trips = MutableStateFlow<List<Trip>>(emptyList())
    val trips: StateFlow<List<Trip>> = _trips

    private val _routes = MutableStateFlow<List<Route>>(emptyList())
    val routes: StateFlow<List<Route>> = _routes

    private val _vehicles = MutableStateFlow<List<Vehicle>>(emptyList())
    val vehicles: StateFlow<List<Vehicle>> = _vehicles

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        fetchAgencies()
    }

    private fun fetchAgencies() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getAgencies()
                _agencies.value = response
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Unknown error"
            }
        }
    }
    fun fetchStops(agencyId: Int) : Job {
        return viewModelScope.launch {
            try {
                _stops.value = RetrofitClient.api.getStops(agencyId)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
    fun fetchStopTimes(agencyId: Int, stopId: Int) : Job {
        return viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.api.getStopTimes(agencyId)
                }
                val filteredStopTimes = response.filter {
                    it.stopId == stopId
                }
                _stopTimes.value = filteredStopTimes
            } catch (e: Exception) {
                _errorMessage.value = "Error loading stop times: ${e.message}"
            }
        }
    }

    fun fetchTrips(agencyId: Int, stopTimes: List<StopTime>) : Job {
        return viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.api.getTrips(agencyId);
                }
                val stopTimesTripIds = stopTimes.map(StopTime::tripId)
                val filteredTrips = response.filter {
                    stopTimesTripIds.contains(it.tripId)
                }
                _trips.value = filteredTrips
            } catch (e: Exception) {
                _errorMessage.value = "Error loading trips: ${e.message}"
            }
        }
    }

    fun fetchRoutes(agencyId: Int, trips: List<Trip>) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.api.getRoutes(agencyId)
                }
                val tripsRouteIds = trips.map(Trip::routeId)
                val filteredRoutes = response.filter {
                    tripsRouteIds.contains(it.routeId)
                }
                _routes.value = filteredRoutes
            } catch (e: Exception) {
                _errorMessage.value = "Error loading agencies: ${e.message}"
            }

        }
    }
    fun fetchVehicles(agencyId: Int, routeId: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.api.getVehicles(agencyId, routeId) // API call to fetch buses
                }
                _vehicles.value = response
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch vehicles: ${e.message}"
            }
        }
    }
}
