package org.kevinparks.shipmenttracker.strategy

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.kevinparks.shipmenttracker.model.Shipment
import org.kevinparks.shipmenttracker.model.ShipmentType
import org.kevinparks.shipmenttracker.strategy.ShippedUpdateStrategy
import kotlin.test.assertEquals

class ShippedUpdateStrategyTest {

    @Test
    fun `applyUpdate should set expected delivery and return shipped status`() {
        val shipment = Shipment("ship001", createdAt = 1000L, shipmentType = ShipmentType.OVERNIGHT)

        val strategy = ShippedUpdateStrategy()
        val expectedDate = 2000L
        val timestamp = 3000L

        val result = strategy.applyUpdate(shipment, listOf(expectedDate.toString()), timestamp)

        assertEquals(expectedDate, shipment.expectedDeliveryDateTimestamp)
        assertEquals("shipped", result.newStatus)
        assertEquals("created", result.previousStatus)
        assertEquals(timestamp, result.timestamp)
    }

    @Test
    fun `applyUpdate should throw if expected date is missing`() {
        val shipment = Shipment("ship002", createdAt = 0L, shipmentType = ShipmentType.BULK)
        val strategy = ShippedUpdateStrategy()

        val exception = assertThrows<IllegalArgumentException> {
            strategy.applyUpdate(shipment, emptyList(), 123456L)
        }

        assertEquals("Missing expected delivery date", exception.message)
    }
}
