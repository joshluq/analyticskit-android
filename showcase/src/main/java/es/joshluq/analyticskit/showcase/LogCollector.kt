package es.joshluq.analyticskit.showcase

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** Singleton to collect logs for the showcase console view. */
object LogCollector {
    private val _logs = MutableStateFlow<List<String>>(emptyList())
    val logs: StateFlow<List<String>> = _logs.asStateFlow()

    fun addLog(message: String) {
        _logs.update { currentLogs ->
            if (currentLogs.size >= 100) {
                ArrayList<String>(100).apply {
                    addAll(currentLogs.subList(currentLogs.size - 99, currentLogs.size))
                    add(message)
                }
            } else {
                ArrayList<String>(currentLogs.size + 1).apply {
                    addAll(currentLogs)
                    add(message)
                }
            }
        }
    }

    fun clear() {
        _logs.value = emptyList()
    }
}
