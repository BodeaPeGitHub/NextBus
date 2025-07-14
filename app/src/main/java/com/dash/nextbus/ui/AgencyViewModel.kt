package com.dash.nextbus.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dash.nextbus.model.Agency
import com.dash.nextbus.model.Stop
import com.dash.nextbus.model.StopTime
import com.dash.nextbus.service.RetrofitClient
import kotlinx.coroutines.Dispatchers
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
    fun fetchStops(agencyId: Int) {
        viewModelScope.launch {
            try {
                _stops.value = RetrofitClient.api.getStops(agencyId)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
    fun fetchStopTimes(agencyId: Int, stopId: Int) {
        viewModelScope.launch {
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
}
