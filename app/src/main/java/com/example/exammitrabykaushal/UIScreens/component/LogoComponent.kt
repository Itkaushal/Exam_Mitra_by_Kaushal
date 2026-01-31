package com.example.exammitrabykaushal.UIScreens.component

import com.example.exammitrabykaushal.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExamMitraLogo(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        // 1. The Icon Graphic
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1565C0), // Royal Blue
                            Color(0xFFF18A69)  // Light Blue
                        )
                    )
                )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.target_logo), // Graduation cap icon
                contentDescription = "Logo Icon",
                tint = Color.White,
                modifier = Modifier.size(60.dp)
            )

            // A small "Success Tick" badge on the logo
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFFFF9800), // Orange/Saffron
                modifier = Modifier
                    .size(35.dp)
                    .align(Alignment.BottomEnd)
                    .padding(end = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2. The Main Text "Exam Mitra"
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color(0xFF0D47A1), fontWeight = FontWeight.Bold)) {
                    append("Exam ")
                }
                withStyle(style = SpanStyle(color = Color(0xFFFF6F00), fontWeight = FontWeight.Bold)) { // Saffron Color
                    append("Mitra")
                }
            },
            fontSize = 28.sp,
            fontFamily = FontFamily.Serif
        )

        // 3. The Tagline "by Kaushal"
        Text(
            text = "by Kaushal",
            fontSize = 14.sp,
            color = Color.Gray,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}