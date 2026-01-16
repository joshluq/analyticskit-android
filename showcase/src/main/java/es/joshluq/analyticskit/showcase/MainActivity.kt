package es.joshluq.analyticskit.showcase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dagger.hilt.android.AndroidEntryPoint
import es.joshluq.analyticskit.domain.model.AnalyticsEvent
import es.joshluq.analyticskit.sdk.AnalyticskitManager
import es.joshluq.analyticskit.showcase.ui.theme.ShowcaseTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var analyticskitManager: AnalyticskitManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Track screen view on start
        analyticskitManager.track(AnalyticsEvent.ScreenView("MainScreen", "MainActivity"))

        setContent {
            ShowcaseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        AnalyticsShowcaseScreen(
                            onTrackEvent = { name ->
                                analyticskitManager.track(
                                    AnalyticsEvent.Custom(name, mapOf("timestamp" to System.currentTimeMillis())),
                                )
                            },
                            onTrackFunnel = { step ->
                                analyticskitManager.track(AnalyticsEvent.FunnelStep("checkout_funnel", step))
                            },
                            onToggleProvider = { active ->
                                if (active) {
                                    analyticskitManager.addProvider(ConsoleAnalyticsProvider())
                                } else {
                                    analyticskitManager.removeProvider("CONSOLE_PROVIDER")
                                }
                            },
                            onTraceStep = { step, value ->
                                analyticskitManager.traceEvent("purchase_flow", mapOf(step to value))
                            },
                            onTrackTraced = {
                                analyticskitManager.trackTracedEvent("purchase_flow")
                            },
                            onSetGlobalProp = { key, value ->
                                analyticskitManager.addGlobalProperty(key, value)
                            },
                            modifier = Modifier.weight(1f),
                        )
                        ConsoleView(modifier = Modifier.fillMaxWidth().height(200.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ConsoleView(modifier: Modifier = Modifier) {
    val logs by LogCollector.logs.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty()) {
            listState.animateScrollToItem(logs.size - 1)
        }
    }

    Column(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.9f))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Console Logs", color = Color.Green, fontSize = 12.sp)
            Button(
                onClick = { LogCollector.clear() },
                modifier = Modifier.height(24.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 0.dp)
            ) {
                Text("Clear", fontSize = 10.sp)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(color = Color.Green.copy(alpha = 0.5f))
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(logs) { log ->
                Text(
                    text = "> $log",
                    color = Color.LightGray,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun AnalyticsShowcaseScreen(
    onTrackEvent: (String) -> Unit,
    onTrackFunnel: (String) -> Unit,
    onToggleProvider: (Boolean) -> Unit,
    onTraceStep: (String, String) -> Unit,
    onTrackTraced: () -> Unit,
    onSetGlobalProp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isProviderActive by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp).verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Analyticskit Showcase", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        // Provider Toggle Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Console Provider Active")
            Switch(
                checked = isProviderActive,
                onCheckedChange = {
                    isProviderActive = it
                    onToggleProvider(it)
                },
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        // Global Properties Section
        Text(text = "Global Properties", style = MaterialTheme.typography.titleMedium)
        Button(
            onClick = { onSetGlobalProp("user_type", "premium") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Set user_type = premium")
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        // Event Tracing Section
        Text(text = "Event Tracing (Grouped Data)", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { onTraceStep("step_1", "data_A") }, modifier = Modifier.weight(1f)) {
                Text("Trace 1")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onTraceStep("step_2", "data_B") }, modifier = Modifier.weight(1f)) {
                Text("Trace 2")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onTrackTraced,
            modifier = Modifier.fillMaxWidth(),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            )
        ) {
            Text("Send Consolidated Event")
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Text(text = "Standard Tracking", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { onTrackEvent("button_clicked") }, modifier = Modifier.fillMaxWidth()) {
            Text("Track Custom Event")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { onTrackFunnel("step_1") }, modifier = Modifier.weight(1f)) {
                Text("Funnel Step 1")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onTrackFunnel("step_2") }, modifier = Modifier.weight(1f)) {
                Text("Funnel Step 2")
            }
        }
    }
}
