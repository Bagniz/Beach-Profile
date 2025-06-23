package com.example.beachprofile.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.beachprofile.measures.Measure
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.ScatterData
import com.github.mikephil.charting.data.ScatterDataSet

@Composable
fun InclinationGraph(showInclinationChart: MutableState<Boolean>, measures: List<Measure>) {

    Dialog(onDismissRequest = { showInclinationChart.value = false }) {
        Surface(
            modifier = Modifier
                .requiredWidth(400.dp)
                .requiredHeight(300.dp)
                .padding(10.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            AndroidView(
                factory = { context ->
                    ScatterChart(context).apply {
                        setBackgroundColor(android.graphics.Color.WHITE)
                        val entries = measures.mapIndexed { index, measure ->
                            // Combine lat/lon into a single X position
                            val x = index.toFloat()
                            val y = measure.inclination
                            Entry(x, y)
                        }

                        val dataSet = ScatterDataSet(entries, "Inclination").apply {
                            scatterShapeSize = 10f
                        }

                        this.data = ScatterData(dataSet)
                        this.description.text = "Inclination vs Location"
                        this.xAxis.position = XAxis.XAxisPosition.BOTTOM
                        this.axisLeft.resetAxisMinimum()
                        this.xAxis.resetAxisMinimum()
                        this.axisRight.isEnabled = false
                        this.invalidate()
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}