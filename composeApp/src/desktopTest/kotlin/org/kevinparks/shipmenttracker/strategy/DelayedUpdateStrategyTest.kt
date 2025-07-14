package org.kevinparks.shipmenttracker.strategy

import org.kevinparks.shipmenttracker.model.Shipment
import kotlin.test.*

class DelayedUpdateStrategyTest {

    @Test
    fun testApplyUpdate() {
        val strategy = DelayedUpdateStrategy()
        val shipment = Shipment("s1000")
        val update = strategy.applyUpdate(shipment, listOf("1652718051403"), 1234567890)

        assertEquals("delayed", update.newStatus)
    }
}

