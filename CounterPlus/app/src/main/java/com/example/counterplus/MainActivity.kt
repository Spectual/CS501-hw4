package com.example.counterplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CounterViewModel : ViewModel() {

    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count

    private val _autoMode = MutableStateFlow(false)
    val autoMode: StateFlow<Boolean> = _autoMode

    private var autoJob: Job? = null
    private var interval = 3000L

    fun increment() { _count.value += 1 }
    fun decrement() { _count.value -= 1 }
    fun reset() { _count.value = 0 }

    fun toggleAuto() {
        _autoMode.value = !_autoMode.value
        if (_autoMode.value) startAutoIncrement() else stopAutoIncrement()
    }

    private fun startAutoIncrement() {
        autoJob?.cancel()
        autoJob = viewModelScope.launch {
            while (_autoMode.value) {
                delay(interval)
                _count.value += 1
            }
        }
    }

    private fun stopAutoIncrement() {
        autoJob?.cancel()
        autoJob = null
    }

    fun setInterval(seconds: Long) {
        interval = seconds * 1000
    }

    fun getIntervalSeconds(): Long = interval / 1000
}


class MainActivity : ComponentActivity() {

    private val viewModel: CounterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CounterPlus(viewModel)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterPlus(viewModel: CounterViewModel) {
    val count by viewModel.count.collectAsState()
    val autoMode by viewModel.autoMode.collectAsState()
    var showSettings by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Counter++") },
                actions = {
                    IconButton(onClick = { showSettings = true }) {
                        Text("⚙️")
                    }
                }
            )
        }
    ) { padding ->
        if (showSettings) {
            SettingsDialog(
                viewModel = viewModel,
                onDismiss = { showSettings = false }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Count: $count", style = MaterialTheme.typography.headlineMedium)
            Text("Auto mode: ${if (autoMode) "ON" else "OFF"}")

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { viewModel.increment() }) { Text("+1") }
                Button(onClick = { viewModel.decrement() }) { Text("-1") }
                Button(onClick = { viewModel.reset() }) { Text("Reset") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { viewModel.toggleAuto() }) {
                Text(if (autoMode) "Stop Auto" else "Start Auto")
            }
        }
    }
}


@Composable
fun SettingsDialog(viewModel: CounterViewModel, onDismiss: () -> Unit) {
    var input by remember { mutableStateOf(viewModel.getIntervalSeconds().toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                input.toLongOrNull()?.let { viewModel.setInterval(it) }
                onDismiss()
            }) { Text("Save") }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Settings") },
        text = {
            Column {
                Text("Set auto-increment interval (seconds):")
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    label = { Text("Seconds") }
                )
            }
        }
    )
}