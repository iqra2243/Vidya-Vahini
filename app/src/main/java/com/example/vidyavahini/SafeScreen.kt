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
fun SafeScreen() {

    val context = LocalContext.current

    var selectedStudent by remember { mutableStateOf<Student?>(null) }
    var selectedPlace by remember { mutableStateOf("Home") }
    var message by remember { mutableStateOf("") }

    val database = FirebaseDatabase.getInstance(
        "https://vidyavahini-9c40f-default-rtdb.firebaseio.com/"
    )

    val safeRef = database.getReference("safe_status")
    val historyRef = database.getReference("history")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text("Reached Safely ✅", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Select Student")

        StudentData.students.forEach { student ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {

                    Text(student.name)
                    Text("📞 ${student.contact}")

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                        Button(onClick = { selectedStudent = student }) {
                            Text(if (selectedStudent == student) "Selected ✅" else "Select")
                        }

                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${student.contact}")
                                }
                                context.startActivity(intent)
                            }
                        ) {
                            Text("Call Parent 📞")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Reached Where?")

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Home", "College").forEach {
                Button(onClick = { selectedPlace = it }) {
                    Text(if (selectedPlace == it) "$it ✅" else it)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (selectedStudent == null) {
                    message = "Select student first ❗"
                    return@Button
                }

                val time = TimeUtils.currentTime()

                val update =
                    "✅ ${selectedStudent!!.name} reached $selectedPlace safely\nTime: $time"

                safeRef.setValue(update)

                historyRef.push().setValue(
                    HistoryItem(
                        type = "Safe Reach",
                        message = "${selectedStudent!!.name} reached $selectedPlace",
                        time = time
                    )
                )

                message = "Update sent + Parent can be called 📞"
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Confirm Safe Reach")
        }

        Text(message, modifier = Modifier.padding(top = 12.dp))

        Spacer(modifier = Modifier.height(60.dp))
    }
}