package org.kevinparks.shipmenttracker.strategy

import org.kevinparks.shipmenttracker.model.Shipment
import kotlin.test.*

class DeliveredUpdateStrategyTest {

    @Test
    fun testApplyUpdate() {
        val strategy = DeliveredUpdateStrategy()
        val shipment = Shipment("s1000")
        val update = strategy.applyUpdate(shipment, emptyList(), 1234567890)

        assertEquals("delivered", update.newStatus)
    }
}

