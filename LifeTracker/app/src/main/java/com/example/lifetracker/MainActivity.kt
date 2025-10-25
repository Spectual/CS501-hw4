package com.example.lifetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class LifecycleEventLog(val event: String, val time: String, val color: Color)

class LifeTrackerViewModel : ViewModel() {
    private val _logs = mutableStateListOf<LifecycleEventLog>()
    val logs: List<LifecycleEventLog> get() = _logs

    fun addEvent(event: String) {
        val color = when (event) {
            "ON_CREATE" -> Color(0xFF64B5F6)
            "ON_START" -> Color(0xFF81C784)
            "ON_RESUME" -> Color(0xFFFFF176)
            "ON_PAUSE" -> Color(0xFFFFB74D)
            "ON_STOP" -> Color(0xFFE57373)
            "ON_DESTROY" -> Color(0xFF9E9E9E)
            else -> Color.Gray
        }
        val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        _logs.add(0, LifecycleEventLog(event, time, color))
    }
}

class MainActivity : ComponentActivity(), LifecycleEventObserver {

    private val viewModel: LifeTrackerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(this)
        viewModel.addEvent("onCreate")

        setContent {
            LifeTrackerApp(viewModel)
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        val name = event.name
        if (name != "ON_ANY") viewModel.addEvent(name)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(this)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifeTrackerApp(viewModel: LifeTrackerViewModel) {
    val logs = viewModel.logs
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopAppBar(title = { Text("LifeTracker") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                items(logs) { log ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .background(log.color.copy(alpha = 0.2f))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(log.event, color = log.color, style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(log.time, color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            if (logs.isNotEmpty()) {
                val latest = logs.first().event
                LaunchedEffect(latest) {
                    scope.launch {
                        snackbarHostState.showSnackbar("Lifecycle changed: $latest")
                    }
                }
            }
        }
    }
}