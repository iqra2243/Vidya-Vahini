package com.example.vidyavahini

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun VisualRouteLine(
    stops: List<String>,
    currentStop: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            stops.forEachIndexed { index, stop ->

                val isCurrent = stop.equals(currentStop, ignoreCase = true)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(if (isCurrent) 18.dp else 12.dp)
                            .background(
                                color = if (isCurrent) Color(0xFF7E57C2) else Color.LightGray,
                                shape = CircleShape
                            )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (isCurrent) "📍 $stop" else stop,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                if (index != stops.lastIndex) {
                    Box(
                        modifier = Modifier
                            .height(3.dp)
                            .weight(0.6f)
                            .background(Color(0xFFB39DDB))
                    )
                }
            }
        }
    }
}