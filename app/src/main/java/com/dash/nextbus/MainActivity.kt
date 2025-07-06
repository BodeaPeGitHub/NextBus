package com.dash.nextbus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dash.nextbus.service.RetrofitClient
import com.dash.nextbus.ui.AgencyViewModel
import com.dash.nextbus.ui.theme.NextBusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NextBusTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StationSelectorScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun StationSelectorScreen(
    modifier: Modifier = Modifier,
    agencyViewModel: AgencyViewModel = viewModel()
) {
    var hasInternet by remember { mutableStateOf<Boolean?>(null) }

    // Launch internet check once when composable enters composition
    LaunchedEffect(Unit) {
        hasInternet = RetrofitClient.canReachGoogle()
    }

    when (hasInternet) {
        null -> {
            // Loading indicator while checking internet
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        false -> {
            // Show no internet error
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No internet connection. Please check your connection.", color = MaterialTheme.colorScheme.error)
            }
        }
        true -> {
            // Internet is available, proceed with loading agencies and UI
            val agencies by agencyViewModel.agencies.collectAsState()
            val error by agencyViewModel.errorMessage.collectAsState()

            val stationList = agencies.map { it.agency_name }
            var selectedStation by remember { mutableStateOf("") }
            var expanded by remember { mutableStateOf(false) }
            val favoriteStations = remember { mutableStateListOf<String>() }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Select a Station to Add to Favorites")

                if (error != null) {
                    Text("Error loading agencies: $error", color = MaterialTheme.colorScheme.error)
                }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = selectedStation,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Choose station") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        stationList.forEach { station ->
                            DropdownMenuItem(
                                text = { Text(station) },
                                onClick = {
                                    selectedStation = station
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        if (selectedStation.isNotEmpty() && !favoriteStations.contains(selectedStation)) {
                            favoriteStations.add(selectedStation)
                        }
                    },
                    enabled = selectedStation.isNotEmpty()
                ) {
                    Text("Add to Favorites")
                }

                // Show favorite stations
                Text("Favorite Stations:")
                favoriteStations.forEach { station ->
                    Text("• $station")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun StationSelectorScreenPreview() {
    NextBusTheme {
        StationSelectorScreen()
    }
}
