package org.kevinparks.shipmenttracker.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kevinparks.shipmenttracker.model.ShipmentFactory
import org.kevinparks.shipmenttracker.model.ShipmentType

class ShipmentFactoryTest {

    @Test
    fun `createShipment should initialize shipment with given data`() {
        val shipment = ShipmentFactory.createShipment(
            id = "XYZ123",
            type = ShipmentType.EXPRESS,
            createdAt = 123456789L
        )

        assertEquals("XYZ123", shipment.id)
        assertEquals(ShipmentType.EXPRESS, shipment.shipmentType)
        assertEquals(123456789L, shipment.createdAt)
    }

    @Test
    fun `parseType should return correct ShipmentType for known strings`() {
        assertEquals(ShipmentType.EXPRESS, ShipmentFactory.parseType("EXPRESS"))
        assertEquals(ShipmentType.STANDARD, ShipmentFactory.parseType("standard"))
        assertEquals(ShipmentType.OVERNIGHT, ShipmentFactory.parseType("Overnight"))
        assertEquals(ShipmentType.BULK, ShipmentFactory.parseType("bulk"))
    }

    @Test
    fun `parseType should throw exception for unknown strings`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            ShipmentFactory.parseType("UNKNOWN_TYPE")
        }
        assertEquals("Unknown ShipmentType: UNKNOWN_TYPE", exception.message)
    }
}
