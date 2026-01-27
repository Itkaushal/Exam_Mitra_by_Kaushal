package com.example.exammitrabykaushal.UIScreens.screen.chart_scrren

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.exammitrabykaushal.DataLayer.TestResult
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

@Composable
fun AccuracyPieChart(historyList: List<TestResult>) {

    if (historyList.isEmpty()) return

    // Correct accuracy calculation
    val avgAccuracy = historyList.map {
        if (it.totalQuestions > 0)
            (it.correctCount.toFloat() / it.totalQuestions) * 100f
        else 0f
    }.average().toFloat()

    val correct = avgAccuracy.coerceIn(0f, 100f)
    val wrong = 100f - correct

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        factory = { context ->
            PieChart(context).apply {

                // Chart behavior
                description.isEnabled = false
                legend.isEnabled = true
                isRotationEnabled = false
                isHighlightPerTapEnabled = true

                // Donut style
                isDrawHoleEnabled = true
                holeRadius = 60f
                transparentCircleRadius = 65f

                // Center text
                setDrawCenterText(true)
                centerText = "Accuracy\n${correct.toInt()}%"
                setCenterTextSize(16f)
                setCenterTextTypeface(android.graphics.Typeface.DEFAULT_BOLD)

                // Labels
                setDrawEntryLabels(false)
                setUsePercentValues(true)

                animateY(900)
            }
        },
        update = { chart ->

            val entries = listOf(
                PieEntry(correct, "Correct"),
                PieEntry(wrong, "Wrong")
            )

            val dataSet = PieDataSet(entries, "").apply {
                colors = listOf(
                    android.graphics.Color.parseColor("#43A047"), // Green
                    android.graphics.Color.parseColor("#E53935")  // Red
                )
                sliceSpace = 2f
                valueTextSize = 12f
                valueTextColor = android.graphics.Color.WHITE
            }

            chart.data = PieData(dataSet)
            chart.invalidate()
        }
    )
}

