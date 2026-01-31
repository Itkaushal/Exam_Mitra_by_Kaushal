package com.example.exammitrabykaushal.UIScreens.screen.testscreen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exammitrabykaushal.model.Question
import com.example.exammitrabykaushal.model.TestQuestion

@Composable
fun ReviewQuestionCardForTestScreen(q: TestQuestion, userSelected: Int?) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(q.text, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            q.options.forEachIndexed { i, opt ->
                val isCorrect = i == q.correctIndex
                val isUser = userSelected == i
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .border(
                                width = 1.dp,
                                color = if (isCorrect) Color.Green else if (isUser) Color.Red else Color.LightGray,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${'A' + i}", fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(
                        opt,
                        color = when {
                            isCorrect -> Color(0xFF0F9D58)
                            isUser -> MaterialTheme.colorScheme.primary
                            else -> Color.Unspecified
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            // Explanation
            Text("Explanation:", fontWeight = FontWeight.Medium)
            Text(q.explanation, color = Color.Gray)
        }
    }
}