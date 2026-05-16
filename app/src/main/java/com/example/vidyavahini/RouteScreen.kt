package com.example.vidyavahini

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RouteScreen(navController: NavController) {

    var selectedIndex by remember { mutableStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Select Your Route 🗺️",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Choose your college/school commute route.",
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(18.dp))

        RouteData.routes.forEachIndexed { index, route ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .clickable { selectedIndex = index }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(route.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = route.stops.joinToString("  →  "),
                        modifier = Modifier.padding(top = 6.dp)
                    )

                    if (selectedIndex == index) {
                        Text(
                            text = "Selected ✅",
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }

        if (selectedIndex != -1) {
            Button(
                onClick = { navController.navigate("ping/$selectedIndex") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text("Continue to Bus Ping 📍")
            }
        }
    }
}