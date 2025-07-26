package org.kevinparks.shipmenttracker.strategy

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.kevinparks.shipmenttracker.model.Shipment
import org.kevinparks.shipmenttracker.model.ShipmentType
import org.kevinparks.shipmenttracker.strategy.DelayedUpdateStrategy
import kotlin.test.assertEquals

class DelayedUpdateStrategyTest {

    @Test
    fun `applyUpdate should set expectedDeliveryDate and return delayed status`() {
        val shipment = Shipment("delay001", createdAt = 1_000_000L, shipmentType = ShipmentType.EXPRESS)

        val strategy = DelayedUpdateStrategy()
        val newExpected = 1_300_000L
        val result = strategy.applyUpdate(shipment, listOf(newExpected.toString()), 2_000_000L)

        assertEquals(newExpected, shipment.expectedDeliveryDateTimestamp)
        assertEquals("delayed", result.newStatus)
        assertEquals("created", result.previousStatus)
        assertEquals(2_000_000L, result.timestamp)
    }

    @Test
    fun `applyUpdate should throw if no expected date provided`() {
        val shipment = Shipment("delay002", createdAt = 0L, shipmentType = ShipmentType.STANDARD)

        val strategy = DelayedUpdateStrategy()
        val exception = assertThrows<IllegalArgumentException> {
            strategy.applyUpdate(shipment, emptyList(), 123456L)
        }

        assertEquals("Missing new expected delivery date", exception.message)
    }
}
