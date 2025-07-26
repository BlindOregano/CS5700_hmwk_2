package org.kevinparks.shipmenttracker.model

import org.junit.jupiter.api.*
import org.kevinparks.shipmenttracker.model.*

import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.assertFalse

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ShipmentRepositoryTest {

    private val shipment1 = Shipment("S1", 1000L, ShipmentType.STANDARD)
    private val shipment2 = Shipment("S2", 2000L, ShipmentType.EXPRESS)

    @BeforeEach
    fun clearRepo() {
        ShipmentRepository.clearAll()
    }

    @Test
    fun `addShipment should store shipment by id`() {
        ShipmentRepository.addShipment(shipment1)
        assertEquals(shipment1, ShipmentRepository.getShipment("S1"))
    }

    @Test
    fun `getShipment should return null for unknown id`() {
        assertNull(ShipmentRepository.getShipment("NON_EXISTENT"))
    }

    @Test
    fun `contains should return true only for existing ids`() {
        ShipmentRepository.addShipment(shipment1)
        assertTrue(ShipmentRepository.contains("S1"))
        assertFalse(ShipmentRepository.contains("S2"))
    }

    @Test
    fun `getAllShipments should return all added shipments`() {
        ShipmentRepository.addShipment(shipment1)
        ShipmentRepository.addShipment(shipment2)

        val all = ShipmentRepository.getAllShipments()
        assertEquals(2, all.size)
        assertTrue(all.containsAll(listOf(shipment1, shipment2)))
    }

    @Test
    fun `clearAll should remove all shipments`() {
        ShipmentRepository.addShipment(shipment1)
        ShipmentRepository.addShipment(shipment2)
        ShipmentRepository.clearAll()

        assertEquals(0, ShipmentRepository.getAllShipments().size)
    }
}
