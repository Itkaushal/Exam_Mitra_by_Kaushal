package com.example.exammitrabykaushal.UIScreens.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.random.Random

// Confetti (lightweight, purely Compose)
@Composable
fun ConfettiAnimation(count: Int = 30) {
    val density = LocalDensity.current
    val widthPx = with(density) { 360.dp.toPx() } // approx - the canvas will scale
    val heightPx = with(density) { 600.dp.toPx() }

    // particles data
    val particles = remember {
        List(count) {
            Particle(
                x = Random.nextFloat() * widthPx,
                y = -Random.nextFloat() * 200f,
                vx = (Random.nextFloat() - 0.5f) * 80f,
                vy = Random.nextFloat() * 200f + 100f,
                size = Random.nextFloat() * 12f + 6f,
                color = listOf(Color(0xFFE53935), Color(0xFFFB8C00), Color(0xFFFFEB3B), Color(0xFF43A047), Color(0xFF1E88E5)).random(),
                rotation = Random.nextFloat() * 360f,
                rotSpeed = (Random.nextFloat() - 0.5f) * 200f
            )
        }
    }

    // animated state
    val elapsed = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        elapsed.animateTo(1f, animationSpec = infiniteRepeatable(tween(2500, easing = LinearEasing)))
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val t = elapsed.value
        for (p in particles) {
            val nx = p.x + p.vx * t
            val ny = p.y + p.vy * t + 0.5f * 300f * t * t // gravity-ish
            rotate(p.rotation + p.rotSpeed * t, pivot = Offset(nx, ny)) {
                drawRect(color = p.color, topLeft = Offset(nx, ny), size = androidx.compose.ui.geometry.Size(p.size, p.size))
            }
        }
    }
}

private data class Particle(
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val size: Float,
    val color: Color,
    val rotation: Float,
    val rotSpeed: Float
)

// helper
fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%02d:%02d".format(m, s)
}