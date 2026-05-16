package com.example.vidyavahini

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.database.*

@Composable
fun HomeScreen(navController: NavController) {

    val context = LocalContext.current

    var busStatus by remember { mutableStateOf("No bus update yet") }
    var busLocation by remember { mutableStateOf("") }
    var selectedRouteIndex by remember { mutableStateOf(0) }
    var safeStatus by remember { mutableStateOf("No safe-reach update yet") }

    val database = FirebaseDatabase.getInstance(
        "https://vidyavahini-9c40f-default-rtdb.firebaseio.com/"
    )

    val statusRef = database.getReference("bus_status")
    val locationRef = database.getReference("bus_location")
    val routeRef = database.getReference("selected_route_index")
    val safeRef = database.getReference("safe_status")

    LaunchedEffect(Unit) {

        statusRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                busStatus = snapshot.getValue(String::class.java) ?: "No bus update yet"
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        locationRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                busLocation = snapshot.getValue(String::class.java) ?: ""
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        routeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                selectedRouteIndex = snapshot.getValue(Int::class.java) ?: 0
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        safeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                safeStatus = snapshot.getValue(String::class.java) ?: "No safe-reach update yet"
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    val route = RouteData.getRoute(selectedRouteIndex)

    val updateColor = when {
        busStatus.contains("Breakdown", true) -> Color(0xFFFFE0E0)
        busStatus.contains("Delay", true) -> Color(0xFFFFF4E0)
        else -> Color(0xFFEDE7F6)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7F6))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Vidya-Vahini 🚍", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Student Commute Buddy")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = updateColor)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Latest Bus Update", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(6.dp))
                Text(busStatus)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp)) {

                Text("Selected Route", style = MaterialTheme.typography.titleSmall)

                Spacer(modifier = Modifier.height(4.dp))

                Text(route.name)

                Spacer(modifier = Modifier.height(12.dp))

                Text("Bus Route Map", style = MaterialTheme.typography.titleSmall)

                Spacer(modifier = Modifier.height(12.dp))

                VisualRouteLine(
                    stops = route.stops,
                    currentStop = busLocation
                )

                if (busLocation.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedButton(
                        onClick = {
                            MapUtils.openLocation(context, busLocation)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Open $busLocation on Google Maps 🗺️")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Safe Reach", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(6.dp))
                Text(safeStatus)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Quick Actions", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = { navController.navigate("route") },
                modifier = Modifier.weight(1f)
            ) {
                Text("Ping")
            }

            Button(
                onClick = { navController.navigate("report") },
                modifier = Modifier.weight(1f)
            ) {
                Text("Breakdown")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = { navController.navigate("delay") },
                modifier = Modifier.weight(1f)
            ) {
                Text("Delay")
            }

            Button(
                onClick = { navController.navigate("safe") },
                modifier = Modifier.weight(1f)
            ) {
                Text("Safe")
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedButton(
            onClick = { navController.navigate("history") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View History 📜")
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}