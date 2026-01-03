package es.joshluq.analyticskit.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class AnalyticsEventTest {

    @Test
    fun `Custom event holds correct data`() {
        val properties = mapOf("key" to "value")
        val event = AnalyticsEvent.Custom("name", properties)

        assertEquals("name", event.name)
        assertEquals(properties, event.properties)
    }

    @Test
    fun `ScreenView event holds correct data`() {
        val event = AnalyticsEvent.ScreenView("Home", "HomeFragment")

        assertEquals("Home", event.screenName)
        assertEquals("HomeFragment", event.screenClass)
    }

    @Test
    fun `FunnelStep event holds correct data`() {
        val properties = mapOf("step" to 1)
        val event = AnalyticsEvent.FunnelStep("Checkout", "Payment", properties)

        assertEquals("Checkout", event.funnelName)
        assertEquals("Payment", event.stepName)
        assertEquals(properties, event.properties)
    }
}
