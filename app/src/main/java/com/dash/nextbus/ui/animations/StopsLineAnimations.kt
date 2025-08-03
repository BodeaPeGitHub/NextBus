package com.dash.nextbus.ui.animations


import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dash.nextbus.model.Stop
@Composable
fun StopsLineAnimation(stops: List<Stop>) {
    val infiniteTransition = rememberInfiniteTransition()
    val alphaAnim = infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp)
        ) {
            val spacing = size.width / (stops.size - 1).coerceAtLeast(1)
            val centerY = size.height / 2

            drawLine(
                color = Color.Blue,
                start = androidx.compose.ui.geometry.Offset(0f, centerY),
                end = androidx.compose.ui.geometry.Offset(size.width, centerY),
                strokeWidth = 6f
            )

            stops.forEachIndexed { index, _ ->
                val x = spacing * index
                drawCircle(
                    color = Color.Blue.copy(alpha = alphaAnim.value),
                    radius = 12f,
                    center = androidx.compose.ui.geometry.Offset(x, centerY)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            stops.forEach { stop ->
                Text(
                    text = stop.stopName,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.width(80.dp)
                )
            }
        }
    }
}
