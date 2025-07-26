package org.kevinparks.shipmenttracker.strategy

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.kevinparks.shipmenttracker.model.Shipment
import org.kevinparks.shipmenttracker.model.ShipmentType
import org.kevinparks.shipmenttracker.strategy.NoteAddedUpdateStrategy
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NoteAddedUpdateStrategyTest {

    @Test
    fun `applyUpdate should add note and preserve status`() {
        val shipment = Shipment("note001", createdAt = 1000L, shipmentType = ShipmentType.STANDARD)
        shipment.addUpdate(org.kevinparks.shipmenttracker.model.ShippingUpdate("created", "processing", 1500L))

        val strategy = NoteAddedUpdateStrategy()
        val timestamp = 2000L
        val result = strategy.applyUpdate(shipment, listOf("Package delayed due to weather"), timestamp)

        assertTrue("Package delayed due to weather" in shipment.getNotes())
        assertEquals("processing", result.previousStatus)
        assertEquals("processing", result.newStatus)
        assertEquals(timestamp, result.timestamp)
    }

    @Test
    fun `applyUpdate should throw if no note is provided`() {
        val shipment = Shipment("note002", createdAt = 1000L, shipmentType = ShipmentType.BULK)
        val strategy = NoteAddedUpdateStrategy()

        val exception = assertThrows<IllegalArgumentException> {
            strategy.applyUpdate(shipment, emptyList(), 2000L)
        }

        assertEquals("Missing note text", exception.message)
    }
}
