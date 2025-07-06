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
import com.dash.nextbus.model.Agency
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationSelectorScreen(
    modifier: Modifier = Modifier,
    agencyViewModel: AgencyViewModel = viewModel()
) {
    val agencies by agencyViewModel.agencies.collectAsState()
    val stops by agencyViewModel.stops.collectAsState()
    val error by agencyViewModel.errorMessage.collectAsState()


    var selectedAgency by remember { mutableStateOf<Agency?>(null) }
    var agencyDropdownExpanded by remember { mutableStateOf(false) }

    var selectedStop by remember { mutableStateOf("") }
    var stopDropdownExpanded by remember { mutableStateOf(false) }

    val favoriteStops = remember { mutableStateListOf<String>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Select City (Agency):")

        // Agency dropdown
        ExposedDropdownMenuBox(
            expanded = agencyDropdownExpanded,
            onExpandedChange = { agencyDropdownExpanded = !agencyDropdownExpanded }
        ) {
            TextField(
                value = selectedAgency?.agency_name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Choose city") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = agencyDropdownExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = agencyDropdownExpanded,
                onDismissRequest = { agencyDropdownExpanded = false }
            ) {
                agencies.forEach { agency ->
                    DropdownMenuItem(
                        text = { Text(agency.agency_name) },
                        onClick = {
                            selectedAgency = agency
                            selectedStop = ""
                            agencyDropdownExpanded = false
                            agencyViewModel.fetchStops(agency.agency_id)
                        }
                    )
                }
            }
        }

        if (selectedAgency != null) {
            Text("Select Station:")

            ExposedDropdownMenuBox(
                expanded = stopDropdownExpanded,
                onExpandedChange = { stopDropdownExpanded = !stopDropdownExpanded }
            ) {
                TextField(
                    value = selectedStop,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Choose station") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = stopDropdownExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = stopDropdownExpanded,
                    onDismissRequest = { stopDropdownExpanded = false }
                ) {
                    stops.forEach { stop ->
                        DropdownMenuItem(
                            text = { Text(stop.stop_name) },
                            onClick = {
                                selectedStop = stop.stop_name
                                stopDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        if (selectedStop.isNotEmpty()) {
            Button(
                onClick = {
                    if (!favoriteStops.contains(selectedStop)) {
                        favoriteStops.add(selectedStop)
                    }
                }
            ) {
                Text("Add to Favorites")
            }
        }

        if (favoriteStops.isNotEmpty()) {
            Text("Favorite Stations:")
            favoriteStops.forEach { station ->
                Text("• $station")
            }
        }

        error?.let {
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
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
