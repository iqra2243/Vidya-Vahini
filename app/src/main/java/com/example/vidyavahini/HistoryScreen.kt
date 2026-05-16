package com.example.vidyavahini

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.database.*

@Composable
fun HistoryScreen() {

    var historyList by remember { mutableStateOf(listOf<HistoryItem>()) }
    var selectedFilter by remember { mutableStateOf("All") }

    val database = FirebaseDatabase.getInstance(
        "https://vidyavahini-9c40f-default-rtdb.firebaseio.com/"
    )
    val historyRef = database.getReference("history")

    LaunchedEffect(Unit) {
        historyRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList = mutableListOf<HistoryItem>()
                for (item in snapshot.children) {
                    val history = item.getValue(HistoryItem::class.java)
                    if (history != null) tempList.add(history)
                }
                historyList = tempList.reversed()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    val filteredList =
        if (selectedFilter == "All") historyList
        else historyList.filter { it.type == selectedFilter }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text("Update History 📜", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { historyRef.removeValue() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clear All History 🗑️")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("All", "Bus Ping", "Breakdown", "Safe Reach", "Delay").forEach {
                Button(onClick = { selectedFilter = it }) {
                    Text(it)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        filteredList.forEach { item ->

            val color = when (item.type) {
                "Breakdown" -> Color(0xFFFFE0E0)
                "Safe Reach" -> Color(0xFFE0FFE6)
                "Delay" -> Color(0xFFFFF4E0)
                else -> Color(0xFFE8F1FF)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                colors = CardDefaults.cardColors(containerColor = color)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(item.type, style = MaterialTheme.typography.titleMedium)
                    Text(item.message)
                    Text("Time: ${item.time}")
                }
            }
        }
    }
}