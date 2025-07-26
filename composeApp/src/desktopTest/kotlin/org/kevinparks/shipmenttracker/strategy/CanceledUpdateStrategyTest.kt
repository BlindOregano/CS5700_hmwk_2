package org.kevinparks.shipmenttracker.strategy

import org.junit.jupiter.api.Test
import org.kevinparks.shipmenttracker.model.Shipment
import org.kevinparks.shipmenttracker.model.ShipmentType
import org.kevinparks.shipmenttracker.strategy.CanceledUpdateStrategy
import kotlin.test.assertEquals

class CanceledUpdateStrategyTest {

    @Test
    fun `applyUpdate returns ShippingUpdate with canceled status`() {
        val shipment = Shipment("cancel123", createdAt = 123456L, shipmentType = ShipmentType.EXPRESS)
        shipment.addNote("Preparing to cancel")
        shipment.addUpdate(org.kevinparks.shipmenttracker.model.ShippingUpdate("created", "in transit", 2000L))

        val strategy = CanceledUpdateStrategy()
        val result = strategy.applyUpdate(shipment, emptyList(), 999999L)

        assertEquals("in transit", result.previousStatus)
        assertEquals("canceled", result.newStatus)
        assertEquals(999999L, result.timestamp)
    }
}
