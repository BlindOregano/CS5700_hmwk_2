package org.kevinparks.shipmenttracker.strategy

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.kevinparks.shipmenttracker.model.Shipment
import org.kevinparks.shipmenttracker.model.ShipmentType
import org.kevinparks.shipmenttracker.strategy.LocationUpdateStrategy
import kotlin.test.assertEquals

class LocationUpdateStrategyTest {

    @Test
    fun `applyUpdate should set new location and preserve status`() {
        val shipment = Shipment("loc001", createdAt = 123456L, shipmentType = ShipmentType.BULK)
        shipment.addUpdate(org.kevinparks.shipmenttracker.model.ShippingUpdate("created", "processing", 1500L))

        val strategy = LocationUpdateStrategy()
        val timestamp = 999999L
        val result = strategy.applyUpdate(shipment, listOf("Phoenix Warehouse"), timestamp)

        assertEquals("processing", result.previousStatus)
        assertEquals("processing", result.newStatus)
        assertEquals(timestamp, result.timestamp)
        assertEquals("Phoenix Warehouse", shipment.currentLocation)
    }

    @Test
    fun `applyUpdate should throw if no location argument is given`() {
        val shipment = Shipment("loc002", createdAt = 0L, shipmentType = ShipmentType.STANDARD)

        val strategy = LocationUpdateStrategy()
        val exception = assertThrows<IllegalArgumentException> {
            strategy.applyUpdate(shipment, emptyList(), 1000L)
        }

        assertEquals("Missing location date", exception.message)
    }
}
