package com.example.vidyavahini

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.database.FirebaseDatabase

@Composable
fun DelayScreen() {

    var selectedBus by remember { mutableStateOf<BusInfo?>(null) }
    var message by remember { mutableStateOf("") }

    val database = FirebaseDatabase.getInstance(
        "https://vidyavahini-9c40f-default-rtdb.firebaseio.com/"
    )

    val statusRef = database.getReference("bus_status")
    val historyRef = database.getReference("history")

    fun sendDelay(delayText: String) {
        if (selectedBus == null) {
            message = "Select bus first ❗"
            return
        }

        val time = TimeUtils.currentTime()

        val update =
            "⏳ Bus Delay Reported\n" +
                    "Bus No: ${selectedBus!!.busNumber}\n" +
                    "Status: $delayText\n" +
                    "Time: $time"

        statusRef.setValue(update)

        historyRef.push().setValue(
            HistoryItem(
                type = "Delay",
                message = "${selectedBus!!.busNumber} - $delayText",
                time = time
            )
        )

        message = "Delay update sent ✅"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text(
            text = "Report Delay ⏳",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Select Bus Number",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        BusData.buses.forEach { bus ->
            Button(
                onClick = { selectedBus = bus },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    if (selectedBus == bus) {
                        "${bus.busNumber} ✅"
                    } else {
                        bus.busNumber
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Select Delay Status",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        listOf(
            "Delayed by 10 mins",
            "Delayed by 20 mins",
            "Delayed by 30 mins",
            "Cancelled today"
        ).forEach { delay ->
            Button(
                onClick = { sendDelay(delay) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(delay)
            }
        }

        Text(
            text = message,
            modifier = Modifier.padding(top = 12.dp)
        )

        Spacer(modifier = Modifier.height(60.dp))
    }
}