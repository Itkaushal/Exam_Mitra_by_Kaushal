package com.example.exammitrabykaushal.UIScreens.nineToTwelve

import androidx.compose.runtime.Composable

@Composable
fun Class9Screen(
    onSubjectSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    BaseClassScreen(
        title = "Class 9 Subjects",
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
