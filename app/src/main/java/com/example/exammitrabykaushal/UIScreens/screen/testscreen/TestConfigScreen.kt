package com.example.exammitrabykaushal.UIScreens.screen.testscreen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exammitrabykaushal.ViewModel.CreateTestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestConfigScreen(
    category: String,
    onStartTest: (String, Int, Int) -> Unit,
    onBack: () -> Unit,
    viewModel: CreateTestViewModel = viewModel()
) {

    val questionCount by viewModel.questionCount.collectAsState()
    val timeMinutes = viewModel.calculateTimeMinutes(questionCount)


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Test Configuration",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1565C0),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    onStartTest(category, questionCount, timeMinutes)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = MaterialTheme.shapes.extraLarge,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1565C0)
                )
            ) {
                Text(
                    "Start Test",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()

        ) {

            /* SUBJECT CARD */
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(
                        "SUBJECT",
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.7f)
                    )
                    Text(
                        category.uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            /* QUESTIONS CARD */
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(Modifier.padding(20.dp)) {

                    Text(
                        "NUMBER OF QUESTIONS",
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "$questionCount Questions",
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.height(12.dp))

                    Slider(
                        value = questionCount.toFloat(),
                        onValueChange = {
                            viewModel.updateQuestionCount(it.toInt())
                            viewModel.calculateTimeMinutes(it.toInt())
                        },
                        valueRange = 5f..60f,
                        steps = 55,
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary,
                            inactiveTrackColor = MaterialTheme.colorScheme.primary.copy(0.3f)
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("5", color = MaterialTheme.colorScheme.onSecondaryContainer)
                        Text("60", color = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            /* TIME CARD */
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "TIME DURATION",
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Text(
                        "$timeMinutes min",
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}





