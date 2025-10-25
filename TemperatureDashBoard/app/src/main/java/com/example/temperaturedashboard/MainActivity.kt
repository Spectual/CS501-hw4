package com.example.temperaturedashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import kotlin.random.Random


data class TemperatureReading(val value: Float, val timestamp: String)

class TemperatureViewModel : ViewModel() {

    private val _readings = MutableStateFlow<List<TemperatureReading>>(emptyList())
    val readings: StateFlow<List<TemperatureReading>> = _readings

    private val _isRunning = MutableStateFlow(true)
    val isRunning: StateFlow<Boolean> = _isRunning

    private var job: Job? = null

    init {
        startSimulation()
    }

    private fun startSimulation() {
        job?.cancel()
        job = viewModelScope.launch {
            while (_isRunning.value) {
                delay(2000)
                val newTemp = Random.nextFloat() * 20 + 65 // 65–85°F
                val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                val updated = (listOf(TemperatureReading(newTemp, time)) + _readings.value)
                    .take(20)
                _readings.value = updated
            }
        }
    }

    fun toggleRunning() {
        _isRunning.value = !_isRunning.value
        if (_isRunning.value) startSimulation() else job?.cancel()
    }

    val currentTemp: Float
        get() = _readings.value.firstOrNull()?.value ?: 0f

    val averageTemp: Float
        get() = if (_readings.value.isEmpty()) 0f else _readings.value.map { it.value }.average().toFloat()

    val minTemp: Float
        get() = _readings.value.minOfOrNull { it.value } ?: 0f

    val maxTemp: Float
        get() = _readings.value.maxOfOrNull { it.value } ?: 0f
}


class MainActivity : ComponentActivity() {

    private val viewModel: TemperatureViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemperatureDashboardApp(viewModel)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemperatureDashboardApp(viewModel: TemperatureViewModel) {
    val readings by viewModel.readings.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Temperature Dashboard") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Summary Section
            Text("Current: ${viewModel.currentTemp.roundToInt()}°F")
            Text("Avg: ${viewModel.averageTemp.roundToInt()}°F")
            Text("Min: ${viewModel.minTemp.roundToInt()}°F")
            Text("Max: ${viewModel.maxTemp.roundToInt()}°F")

            Spacer(modifier = Modifier.height(16.dp))

            // Chart Section
            TemperatureChart(readings)

            Spacer(modifier = Modifier.height(16.dp))

            // List Section
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFF5F5F5))
                    .padding(8.dp)
            ) {
                items(readings) { reading ->
                    Text("${reading.timestamp} — ${"%.1f".format(reading.value)}°F")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { viewModel.toggleRunning() }) {
                Text(if (isRunning) "Pause" else "Resume")
            }
        }
    }
}


@Composable
fun TemperatureChart(readings: List<TemperatureReading>) {
    if (readings.isEmpty()) return

    val points = readings.map { it.value }
    val max = points.maxOrNull() ?: 1f
    val min = points.minOrNull() ?: 0f

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color(0xFFE3F2FD))
    ) {
        val stepX = size.width / (points.size.coerceAtLeast(2) - 1)
        val scaleY = if (max == min) 1f else size.height / (max - min)

        for (i in 1 until points.size) {
            val x1 = (i - 1) * stepX
            val y1 = size.height - (points[i - 1] - min) * scaleY
            val x2 = i * stepX
            val y2 = size.height - (points[i] - min) * scaleY
            drawLine(
                color = Color(0xFF1565C0),
                start = Offset(x1, y1),
                end = Offset(x2, y2),
                strokeWidth = 4f
            )
        }
    }
}