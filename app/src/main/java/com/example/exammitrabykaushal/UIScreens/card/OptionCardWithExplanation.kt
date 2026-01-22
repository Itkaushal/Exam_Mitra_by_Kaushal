package com.example.exammitrabykaushal.UIScreens.card

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Option card reused but with explanation param (we hide explanation during test; displayed in review)
@Composable
fun OptionCardWithExplanation(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    showExplanation: Boolean,
    correctIndex: Int,
    explanation: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() }
            .border(width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Color.LightGray.copy(alpha = 0.6f) else Color.LightGray,
                shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor =
            if (isSelected) MaterialTheme.colorScheme.surfaceVariant
            else MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = isSelected, onClick = onClick, colors = RadioButtonDefaults.colors(
                    selectedColor = Color.Blue.copy(alpha = 0.5f),
                    unselectedColor = Color.LightGray
                ))
                Spacer(modifier = Modifier.size(12.dp))
                Text(text, fontSize = 16.sp)
            }
            if (showExplanation) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(explanation, color = Color.Magenta.copy(alpha = 0.5f), modifier = Modifier.padding(12.dp))
            }
        }
    }
}