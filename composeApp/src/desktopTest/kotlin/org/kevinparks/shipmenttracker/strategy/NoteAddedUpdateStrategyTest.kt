package org.kevinparks.shipmenttracker.strategy

import org.kevinparks.shipmenttracker.model.Shipment
import kotlin.test.*

class NoteAddedUpdateStrategyTest {

    @Test
    fun testApplyUpdate() {
        val strategy = NoteAddedUpdateStrategy()
        val shipment = Shipment("s1000")
        val update = strategy.applyUpdate(shipment, listOf("Package was damaged"), 1234567890)

        assertEquals("created", update.newStatus)
    }
}
