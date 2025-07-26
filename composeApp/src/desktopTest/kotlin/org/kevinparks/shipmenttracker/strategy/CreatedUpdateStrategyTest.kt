package org.kevinparks.shipmenttracker.strategy

import org.junit.jupiter.api.Test
import org.kevinparks.shipmenttracker.model.Shipment
import org.kevinparks.shipmenttracker.model.ShipmentType
import org.kevinparks.shipmenttracker.strategy.CreatedUpdateStrategy
import kotlin.test.assertEquals

class CreatedUpdateStrategyTest {

    @Test
    fun `applyUpdate should preserve current status as both previous and new`() {
        val shipment = Shipment("create001", createdAt = 123456L, shipmentType = ShipmentType.BULK)
        shipment.addUpdate(org.kevinparks.shipmenttracker.model.ShippingUpdate("created", "processing", 2000L))

        val strategy = CreatedUpdateStrategy()
        val timestamp = 777777L
        val result = strategy.applyUpdate(shipment, emptyList(), timestamp)

        assertEquals("processing", result.previousStatus)
        assertEquals("processing", result.newStatus)
        assertEquals(timestamp, result.timestamp)
    }
}
