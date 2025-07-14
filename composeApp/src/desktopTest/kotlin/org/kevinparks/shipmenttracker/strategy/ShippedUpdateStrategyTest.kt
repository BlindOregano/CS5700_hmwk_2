package org.kevinparks.shipmenttracker.strategy

import org.kevinparks.shipmenttracker.model.Shipment
import kotlin.test.*

class ShippedUpdateStrategyTest {

    @Test
    fun testApplyUpdate() {
        val strategy = ShippedUpdateStrategy()
        val shipment = Shipment("s1000")
        val update = strategy.applyUpdate(shipment, listOf("1652713940874"), 1234567890)

        assertEquals("shipped", update.newStatus)
    }
}
