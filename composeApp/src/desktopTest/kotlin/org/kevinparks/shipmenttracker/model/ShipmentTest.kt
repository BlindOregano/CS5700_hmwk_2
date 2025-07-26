//package org.kevinparks.shipmenttracker.model

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kevinparks.shipmenttracker.model.*
import org.kevinparks.shipmenttracker.observer.ShipmentObserver
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNull

class ShipmentTest {

    private lateinit var shipment: Shipment
    private lateinit var mockBehavior: ShipmentTypeBehavior
    private val shipmentType = ShipmentType.STANDARD// or whatever default is valid

    @BeforeEach
    fun setUp() {
        mockBehavior = mockk()
        mockkStatic("org.kevinparks.shipmenttracker.model.ShipmentKt") // mocks top-level function
        every { shipmentTypeBehaviorFor(any()) } returns mockBehavior

        shipment = Shipment(
            id = "ABC123",
            createdAt = 1000L,
            shipmentType = shipmentType
        )
    }

    @Test
    fun `addNote should add note and notify observers`() {
        val observer = mockk<ShipmentObserver>(relaxed = true)
        shipment.registerObserver(observer)

        shipment.addNote("Test note")

        assertEquals(listOf("Test note"), shipment.getNotes())
        verify { observer.onShipmentUpdated(shipment) }
    }

    @Test
    fun `addUpdate should update status and notify observers`() {
        val update = ShippingUpdate("created", "shipped", 0)
        shipment.addUpdate(update)

        assertEquals("shipped", shipment.status)
        assertEquals(listOf(update), shipment.getUpdateHistory())
    }

    @Test
    fun `setExpectedDeliveryDate should trigger anomaly check`() {
        every { mockBehavior.validateDeliveryDate(1000L, 2000L) } returns "Too long"

        shipment.setExpectedDeliveryDate(2000L)

        assertTrue(shipment.isAbnormal)
        assertEquals("Too long", shipment.anomalyReason)
    }

    @Test
    fun `setExpectedDeliveryDate with valid data should clear anomaly`() {
        every { mockBehavior.validateDeliveryDate(1000L, 1500L) } returns null

        shipment.setExpectedDeliveryDate(1500L)

        assertFalse(shipment.isAbnormal)
        assertNull(shipment.anomalyReason)
    }

    @Test
    fun `setLocation should update current location and notify observers`() {
        val observer = mockk<ShipmentObserver>(relaxed = true)
        shipment.registerObserver(observer)

        shipment.setLocation("Warehouse B")

        assertEquals("Warehouse B", shipment.currentLocation)
        verify { observer.onShipmentUpdated(shipment) }
    }

    @Test
    fun `removeObserver should prevent future notifications`() {
        val observer = mockk<ShipmentObserver>(relaxed = true)
        shipment.registerObserver(observer)
        shipment.removeObserver(observer)

        shipment.addNote("Will not notify")

        verify(exactly = 0) { observer.onShipmentUpdated(any()) }
    }
}
