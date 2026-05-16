package com.example.vidyavahini

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.database.FirebaseDatabase

@Composable
fun ReportScreen() {

    val context = LocalContext.current

    var selectedBus by remember { mutableStateOf<BusInfo?>(null) }
    var selectedLocation by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val locations = listOf("Village", "Bridge", "Temple", "Market", "College")

    val database = FirebaseDatabase.getInstance(
        "https://vidyavahini-9c40f-default-rtdb.firebaseio.com/"
    )

    val statusRef = database.getReference("bus_status")
    val historyRef = database.getReference("history")

    fun openMap(location: String) {
        val uri = Uri.parse("geo:0,0?q=${Uri.encode(location + " bus stop")}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text("Report Breakdown ⚠️", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Select Bus", style = MaterialTheme.typography.titleMedium)

        BusData.buses.forEach { bus ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Bus: ${bus.busNumber}", style = MaterialTheme.typography.titleMedium)
                    Text("Driver: ${bus.driverName}")
                    Text("📞 ${bus.driverPhone}")

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { selectedBus = bus }
                        ) {
                            Text(if (selectedBus == bus) "Selected ✅" else "Select")
                        }

                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${bus.driverPhone}")
                                }
                                context.startActivity(intent)
                            }
                        ) {
                            Text("Call 📞")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Select Breakdown Location", style = MaterialTheme.typography.titleMedium)

        locations.forEach { location ->
            Button(
                onClick = { selectedLocation = location },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(if (selectedLocation == location) "$location ✅" else location)
            }
        }

        if (selectedLocation.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = { openMap(selectedLocation) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View $selectedLocation on Google Maps 🗺️")
            }
        }

        Button(
            onClick = {
                if (selectedBus == null) {
                    message = "Select a bus first ❗"
                    return@Button
                }

                if (selectedLocation.isBlank()) {
                    message = "Select breakdown location first ❗"
                    return@Button
                }

                val time = TimeUtils.currentTime()

                val update =
                    "⚠️ Breakdown!\n" +
                            "Bus: ${selectedBus!!.busNumber}\n" +
                            "Driver: ${selectedBus!!.driverName}\n" +
                            "Contact: ${selectedBus!!.driverPhone}\n" +
                            "Location: $selectedLocation\n" +
                            "Time: $time"

                statusRef.setValue(update)

                historyRef.push().setValue(
                    HistoryItem(
                        type = "Breakdown",
                        message =
                            "${selectedBus!!.busNumber} broke down at $selectedLocation | " +
                                    "${selectedBus!!.driverName} (${selectedBus!!.driverPhone})",
                        time = time
                    )
                )

                message = "Breakdown alert sent ✅"
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Send Breakdown Alert")
        }

        Text(message, modifier = Modifier.padding(top = 12.dp))

        Spacer(modifier = Modifier.height(60.dp))
    }
}