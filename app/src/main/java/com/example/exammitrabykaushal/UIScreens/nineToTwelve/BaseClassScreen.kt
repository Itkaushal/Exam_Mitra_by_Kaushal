package com.example.exammitrabykaushal.UIScreens.nineToTwelve

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.exammitrabykaushal.UIScreens.nineToTwelve.components.SubjectCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseClassScreen(
    title: String,
    subjects: List<String>,
    onSubjectSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(subjects.size) {
                SubjectCard(
                    subjectName = subjects[it],
                    onClick = { onSubjectSelected(subjects[it]) }
                )
            }
        }
    }
}
