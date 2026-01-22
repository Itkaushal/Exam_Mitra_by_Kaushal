package com.example.exammitrabykaushal.UIScreens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun ExamMitraSplashScreen(onFinish: () -> Unit) {

    val scale = remember { Animatable(0f) }
    var textAlpha by remember { mutableStateOf(0f) }

    LaunchedEffect(true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 900,
                easing = OvershootInterpolatorEasing(2f)
            )
        )
        delay(300)
        textAlpha = 1f

        delay(1500)
        onFinish()
    }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1565C0),
            Color(0xFF42A5F5),
            Color(0xFF64B5F6)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(130.dp)
                    .scale(scale.value)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "EM",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1565C0)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Exam Mitra",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.alpha(textAlpha),
                style = MaterialTheme.typography.headlineMedium.copy(
                    brush = Brush.linearGradient(
                        listOf(
                            Color(0xFFFFC107),
                            Color(0xFFFF5722),
                            Color(0xFF4CAF50),
                            Color(0xFF03A9F4)
                        )
                    )
                )
            )

            Text(
                text = "By Kaushal",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.85f),
                modifier = Modifier
                    .padding(top = 6.dp)
                    .alpha(textAlpha)
            )
        }
    }
}

class OvershootInterpolatorEasing(private val tension: Float) : Easing {
    override fun transform(fraction: Float): Float {
        val t = fraction - 1f
        return t * t * ((tension + 1) * t + tension) + 1f
    }
}
