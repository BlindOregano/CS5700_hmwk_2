package org.kevinparks.shipmenttracker.strategy

import org.kevinparks.shipmenttracker.model.Shipment
import kotlin.test.*

class CreatedUpdateStrategyTest {
    @Test
    fun testApplyUpdateSetsCreatedStatus() {
        val strategy = CreatedUpdateStrategy()
        val shipment = Shipment("s1000")
        val update = strategy.applyUpdate(shipment, emptyList(), 1111)

        assertEquals("created", update.newStatus)
        assertEquals("created", shipment.status)
    }
}
