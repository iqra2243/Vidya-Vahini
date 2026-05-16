package com.example.vidyavahini

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase

@Composable
fun PingScreen(navController: NavController, routeIndex: Int) {

    val students = listOf("Ayesha", "Rahul", "Priya", "Arjun", "Sneha")

    var selectedStudent by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val route = RouteData.getRoute(routeIndex)

    val database = FirebaseDatabase.getInstance(
        "https://vidyavahini-9c40f-default-rtdb.firebaseio.com/"
    )
    val statusRef = database.getReference("bus_status")
    val locationRef = database.getReference("bus_location")
    val routeRef = database.getReference("selected_route_index")

    fun sendPing(stop: String) {
        if (selectedStudent.isBlank()) {
            message = "Please select student first ❗"
            return
        }

        val eta = RouteData.etaForStop(routeIndex, stop)
        val time = TimeUtils.currentTime()

        val update = if (eta == "Arrived") {
            "$selectedStudent pinged: Bus reached $stop\nETA: Arrived\nTime: $time"
        } else {
            "$selectedStudent pinged: Bus just crossed $stop\nBus expected at destination in $eta\nTime: $time"
        }

        routeRef.setValue(routeIndex)
        locationRef.setValue(stop)
        statusRef.setValue(update)

        database.getReference("history").push().setValue(
            HistoryItem(
                type = "Bus Ping",
                message = if (eta == "Arrived") {
                    "$selectedStudent pinged: Bus reached $stop"
                } else {
                    "$selectedStudent pinged: Bus crossed $stop | ETA: $eta"
                },
                time = time
            )
        )

        message = "Ping sent by $selectedStudent ✅"
        navController.navigate("home")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Bus Ping 📍",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = route.name,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Select Student",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        students.forEach { student ->
            Button(
                onClick = { selectedStudent = student },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    if (selectedStudent == student) {
                        "$student ✅"
                    } else {
                        student
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Tap the landmark where the bus was last seen.",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        route.stops.forEach { stop ->
            Button(
                onClick = { sendPing(stop) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Text("Bus crossed $stop")
            }
        }

        Text(
            text = message,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}