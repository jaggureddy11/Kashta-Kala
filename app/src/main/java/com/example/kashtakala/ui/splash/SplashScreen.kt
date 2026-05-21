package com.example.kashtakala.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kashtakala.ui.catalog.Amber
import com.example.kashtakala.ui.catalog.Cream
import com.example.kashtakala.ui.catalog.WoodLight
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {
    // Animation states
    val scale = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Run logo scale and rotation animation in parallel
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    LaunchedEffect(Unit) {
        // Saw rotation animation
        rotation.animateTo(
            targetValue = 360f,
            animationSpec = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            )
        )
        // Fade in title and subtitle after rotation starts
        textAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = EaseInOut
            )
        )
        // Total delay before navigating
        delay(1200)
        onNavigateToHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2B1602),
                        Color(0xFF130901)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Elegant Canvas Saw / Wood-cutting Animation
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .scale(scale.value),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = size.minDimension / 2.5f

                    // Draw outer saw rings/teeth
                    drawCircle(
                        color = Amber,
                        radius = radius,
                        style = Stroke(width = 4.dp.toPx())
                    )

                    // Draw stylized wood carving inner details
                    for (i in 0 until 12) {
                        val angle = (i * 30 + rotation.value) * (Math.PI / 180f)
                        val startX = (center.x + (radius - 12.dp.toPx()) * Math.cos(angle)).toFloat()
                        val startY = (center.y + (radius - 12.dp.toPx()) * Math.sin(angle)).toFloat()
                        val endX = (center.x + radius * Math.cos(angle)).toFloat()
                        val endY = (center.y + radius * Math.sin(angle)).toFloat()
                        
                        drawLine(
                            color = Amber,
                            start = Offset(startX, startY),
                            end = Offset(endX, endY),
                            strokeWidth = 3.dp.toPx()
                        )
                    }

                    // Inner decorative ring
                    drawCircle(
                        color = WoodLight,
                        radius = radius * 0.7f,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }

                // Middle Text emoji
                Text(
                    text = "🪑",
                    fontSize = 44.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Title
            Text(
                text = "Kashta-Kala",
                color = Amber,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.alpha(textAlpha.value)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle
            Text(
                text = "Digital Design Catalog & Estimator",
                color = Cream.copy(alpha = 0.7f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.alpha(textAlpha.value)
            )
        }
    }
}
