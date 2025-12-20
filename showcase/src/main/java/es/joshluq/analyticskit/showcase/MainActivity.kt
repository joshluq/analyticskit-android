package es.joshluq.analyticskit.showcase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
                    AnalyticsShowcaseScreen(
                        onTrackEvent = { name ->
                            analyticskitManager.track(
                                AnalyticsEvent.Custom(name, mapOf("timestamp" to System.currentTimeMillis()))
                            )
                        },
                        onTrackFunnel = { step ->
                            analyticskitManager.track(
                                AnalyticsEvent.FunnelStep("checkout_funnel", step)
                            )
                        },
                        onToggleProvider = { active ->
                            if (active) {
                                analyticskitManager.addProvider(ConsoleAnalyticsProvider())
                            } else {
                                analyticskitManager.removeProvider("CONSOLE_PROVIDER")
                            }
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AnalyticsShowcaseScreen(
    onTrackEvent: (String) -> Unit,
    onTrackFunnel: (String) -> Unit,
    onToggleProvider: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var isProviderActive by remember { mutableStateOf(true) }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Analyticskit Showcase", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(24.dp))

        // Provider Toggle Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Console Provider Active")
            Switch(
                checked = isProviderActive,
                onCheckedChange = {
                    isProviderActive = it
                    onToggleProvider(it)
                }
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
        
        Text(text = "Event Tracking", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onTrackEvent("button_clicked") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Track Custom Event")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { onTrackFunnel("step_1") },
                modifier = Modifier.weight(1f)
            ) {
                Text("Funnel Step 1")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { onTrackFunnel("step_2") },
                modifier = Modifier.weight(1f)
            ) {
                Text("Funnel Step 2")
            }
        }
    }
}
