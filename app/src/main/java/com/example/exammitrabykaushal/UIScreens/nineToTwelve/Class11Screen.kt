package com.example.exammitrabykaushal.UIScreens.nineToTwelve

import androidx.compose.runtime.Composable

@Composable
fun Class11Screen(
    onSubjectSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    BaseClassScreen(
        title = "Class 11 Subjects",
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
