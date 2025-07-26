package org.kevinparks.shipmenttracker.strategy

import org.junit.jupiter.api.Test
import org.kevinparks.shipmenttracker.model.Shipment
import org.kevinparks.shipmenttracker.model.ShipmentType
import org.kevinparks.shipmenttracker.strategy.LostUpdateStrategy
import kotlin.test.assertEquals

class LostUpdateStrategyTest {

    @Test
    fun `applyUpdate should return ShippingUpdate with lost status`() {
        val shipment = Shipment("lost001", createdAt = 1000L, shipmentType = ShipmentType.STANDARD)
        shipment.addUpdate(org.kevinparks.shipmenttracker.model.ShippingUpdate("created", "in transit", 2000L))

        val strategy = LostUpdateStrategy()
        val timestamp = 3000L
        val result = strategy.applyUpdate(shipment, emptyList(), timestamp)

        assertEquals("in transit", result.previousStatus)
        assertEquals("lost", result.newStatus)
        assertEquals(timestamp, result.timestamp)
    }
}
