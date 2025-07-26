package org.kevinparks.shipmenttracker.strategy

import org.junit.jupiter.api.Test
import org.kevinparks.shipmenttracker.model.Shipment
import org.kevinparks.shipmenttracker.model.ShipmentType
import org.kevinparks.shipmenttracker.strategy.DeliveredUpdateStrategy
import kotlin.test.assertEquals

class DeliveredUpdateStrategyTest {

    @Test
    fun `applyUpdate should return ShippingUpdate with delivered status`() {
        val shipment = Shipment("deliver001", createdAt = 1000L, shipmentType = ShipmentType.STANDARD)
        shipment.addUpdate(org.kevinparks.shipmenttracker.model.ShippingUpdate("created", "out for delivery", 1500L))

        val strategy = DeliveredUpdateStrategy()
        val timestamp = 2000L
        val result = strategy.applyUpdate(shipment, emptyList(), timestamp)

        assertEquals("out for delivery", result.previousStatus)
        assertEquals("delivered", result.newStatus)
        assertEquals(timestamp, result.timestamp)
    }
}
