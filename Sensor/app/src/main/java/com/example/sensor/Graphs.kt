package com.example.sensor

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.combinedchart.CombinedChart
import co.yml.charts.ui.combinedchart.model.CombinedChartData
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine

class Graphs : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                val sensorData by viewModel.sensorData.observeAsState(emptyList())

                if (sensorData.isEmpty()) {
                    return@Column
                }
                val pointsDataX = sensorData.mapIndexed { index, data ->
                    Point(index.toFloat(), data.x)
                }
                val pointsDataY = sensorData.mapIndexed { index, data ->
                    Point(index.toFloat(), data.y)
                }
                val pointsDataZ = sensorData.mapIndexed { index, data ->
                    Point(index.toFloat(), data.z)
                }
                val steps = 5
                val xAxisData = AxisData.Builder()
                    .axisStepSize(100.dp)
                    .backgroundColor(Color.Transparent)
                    .steps(pointsDataX.size - 1)
                    .labelData { i -> i.toString() }
                    .labelAndAxisLinePadding(15.dp)
                    .build()

                val yAxisData = AxisData.Builder()
                    .steps(steps)
                    .backgroundColor(Color.Transparent)
                    .labelAndAxisLinePadding(20.dp)
                    .labelData { i ->
                        val yScale = 100 / steps
                        (i * yScale).toString()
                    }.build()
                val linePlotData = LinePlotData(
                    lines = listOf(
                        Line(
                            dataPoints = pointsDataX,
                            LineStyle(color = Color.Red),
                            IntersectionPoint(),
                            SelectionHighlightPoint(),
                            ShadowUnderLine(),
                            SelectionHighlightPopUp()
                        ),
                        Line(
                            dataPoints = pointsDataY,
                            LineStyle(color = Color.Blue),
                            IntersectionPoint(),
                            SelectionHighlightPoint(),
                            ShadowUnderLine(),
                            SelectionHighlightPopUp()
                        )
                    ),
                )
                val linePlotData2 = LinePlotData(
                    lines = listOf(
                        Line(
                            dataPoints = pointsDataZ,
                            LineStyle(color = Color.Green),
                            IntersectionPoint(),
                            SelectionHighlightPoint(),
                            ShadowUnderLine(),
                            SelectionHighlightPopUp()
                        )
                    ),
                )

                CombinedChart(
                    modifier = Modifier.height(400.dp),
                    combinedChartData = CombinedChartData(
                        combinedPlotDataList = listOf(linePlotData, linePlotData2),
                        xAxisData = xAxisData,
                        yAxisData = yAxisData
                    )
                )
            }
        }
    }
}
