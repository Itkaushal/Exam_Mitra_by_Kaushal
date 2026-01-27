package com.example.exammitrabykaushal.UIScreens.screen.chart_scrren

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.exammitrabykaushal.DataLayer.Entity.TestResult
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun MiniPerformanceChart(history: List<TestResult>) {
    if (history.isEmpty()) return

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = true
                axisRight.isEnabled = true
                axisLeft.isEnabled = true
                xAxis.isEnabled = true
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)
                isAutoScaleMinMaxEnabled = true
                isDoubleTapToZoomEnabled = true
                isHighlightPerDragEnabled = true
                isHighlightPerTapEnabled = true
                setDrawBorders(true)
                setDrawGridBackground(true)
                setDrawMarkers(true)
                setDrawBorders(true)
                setTouchEnabled(true)
                setViewPortOffsets(0f, 0f, 0f, 0f)
            }
        },
        update = { chart ->
            val entries = history
                .takeLast(6)
                .mapIndexed { index, result ->
                    Entry(index.toFloat(), result.score.toFloat())
                }

            val dataSet = LineDataSet(entries, "").apply {
                color = android.graphics.Color.parseColor("#1565C0")
                setDrawValues(false)
                setDrawCircles(false)
                lineWidth = 2.5f
                setDrawFilled(true)
                fillColor = android.graphics.Color.parseColor("#1565C0")
                fillAlpha = 60
            }

            chart.data = LineData(dataSet)
            chart.invalidate()
        }
    )
}
