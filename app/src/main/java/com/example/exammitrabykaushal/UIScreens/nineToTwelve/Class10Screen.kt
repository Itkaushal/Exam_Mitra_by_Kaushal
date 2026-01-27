package com.example.exammitrabykaushal.UIScreens.nineToTwelve

import androidx.compose.runtime.Composable

@Composable
fun Class10Screen(
    onSubjectSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    BaseClassScreen(
        title = "Class 10 Subjects",
        subjects = listOf(
            "Mathematics",
            "Science",
            "Social Science",
            "English",
            "Hindi"
        ),
        onSubjectSelected = onSubjectSelected,
        onBack = onBack
    )
}
