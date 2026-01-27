package com.example.exammitrabykaushal.UIScreens.screen.chart_scrren

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.exammitrabykaushal.DataLayer.Entity.TestResult
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

@Composable
fun PerformanceChart(
    historyList: List<TestResult>,
    modifier: Modifier = Modifier
) {
    if (historyList.isEmpty()) return

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        factory = { context ->
            LineChart(context).apply {

                isAutoScaleMinMaxEnabled = true
                isDoubleTapToZoomEnabled = true
                isHighlightPerDragEnabled = true
                isHighlightPerTapEnabled = true
                setDrawBorders(true)
                setDrawGridBackground(true)
                setDrawMarkers(true)
                setDrawBorders(true)

                description.isEnabled = false
                setTouchEnabled(true)
                setPinchZoom(true)
                legend.isEnabled = false

                // 🔹 X Axis
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    setDrawGridLines(false)
                    valueFormatter = IndexAxisValueFormatter(
                        historyList.indices.map { "Test ${it + 1}" }
                    )
                }

                // 🔹 Y Axis
                axisRight.isEnabled = false
                axisLeft.apply {
                    axisMinimum = 0f
                    axisMaximum = 100f
                    granularity = 10f
                    setDrawGridLines(true)
                }

                animateX(800)
            }
        },
        update = { chart ->
            chart.data = LineData(createAccuracyDataSet(historyList))
            chart.invalidate()
        }
    )
}

private fun createAccuracyDataSet(
    history: List<TestResult>
): LineDataSet {

    val entries = history
        .sortedBy { it.timestamp }
        .mapIndexed { index, result ->
            val accuracy =
                if (result.totalQuestions > 0)
                    (result.correctCount.toFloat() / result.totalQuestions) * 100f
                else 0f

            Entry(index.toFloat(), accuracy)
        }

    return LineDataSet(entries, "Accuracy").apply {

        color = android.graphics.Color.parseColor("#1565C0")
        valueTextSize = 10f

        // 🔹 Line
        lineWidth = 2.5f
        mode = LineDataSet.Mode.CUBIC_BEZIER

        // 🔹 Points
        setDrawCircles(true)
        setCircleColor(android.graphics.Color.parseColor("#1565C0"))
        circleRadius = 4.5f

        // 🔹 Fill
        setDrawFilled(true)
        fillAlpha = 70
        fillColor = android.graphics.Color.parseColor("#90CAF9")

        // 🔹 Values (FIXED)
        valueFormatter = SimplePercentFormatter()
        setDrawValues(true)
    }
}

class SimplePercentFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return "${value.toInt()}%"
    }
}
