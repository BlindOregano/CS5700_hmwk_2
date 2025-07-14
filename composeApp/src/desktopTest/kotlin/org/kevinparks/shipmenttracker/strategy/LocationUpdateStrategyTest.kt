package org.kevinparks.shipmenttracker.strategy

import org.kevinparks.shipmenttracker.model.Shipment
import kotlin.test.*

class LocationUpdateStrategyTest {

    @Test
    fun testApplyUpdate() {
        val strategy = LocationUpdateStrategy()
        val shipment = Shipment("s1000")
        val update = strategy.applyUpdate(shipment, listOf("Los Angeles CA"), 1234567890)

        assertEquals("created", update.newStatus)
    }
}
