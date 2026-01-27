package com.example.exammitrabykaushal.UIScreens.nineToTwelve

import androidx.compose.runtime.Composable

@Composable
fun Class12Screen(
    onSubjectSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    BaseClassScreen(
        title = "Class 12 Subjects",
        subjects = listOf(
            "Physics",
            "Chemistry",
            "Mathematics",
            "Biology",
            "Accountancy",
            "Business Studies",
            "Economics",
            "English"
        ),
        onSubjectSelected = onSubjectSelected,
        onBack = onBack
    )
}
