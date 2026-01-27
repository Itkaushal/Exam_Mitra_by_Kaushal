package com.example.exammitrabykaushal.UIScreens.nineToTwelve

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.exammitrabykaushal.UIScreens.nineToTwelve.components.SubjectActionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassSubjectScreen(
    className: String,
    subject: String,
    onBack: () -> Unit,
    onNotesClick: () -> Unit = {},
    onVideosClick: () -> Unit = {},
    onQuizClick: () -> Unit = {}
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Class $className • $subject") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            SubjectActionCard(
                title = "Notes",
                subtitle = "NCERT & Handwritten Notes",
                icon = Icons.Default.MenuBook,
                onClick = onNotesClick
            )

            SubjectActionCard(
                title = "Video Lectures",
                subtitle = "Concept-wise explanation",
                icon = Icons.Default.PlayCircle,
                onClick = onVideosClick
            )

            SubjectActionCard(
                title = "Practice Quiz",
                subtitle = "MCQs & topic tests",
                icon = Icons.Default.Quiz,
                onClick = onQuizClick
            )
        }
    }
}
