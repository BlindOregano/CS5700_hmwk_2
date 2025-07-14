package org.kevinparks.shipmenttracker.model

import org.kevinparks.shipmenttracker.observer.ShipmentObserver
import kotlin.test.*

class ShipmentTest {

    private lateinit var shipment: Shipment

    @BeforeTest
    fun setUp() {
        shipment = Shipment("s1000")
    }

    @Test
    fun testAddNoteAddsNote() {
        shipment.addNote("Handle with care")
        assertEquals(listOf("Handle with care"), shipment.getNotes())
    }

    @Test
    fun testAddUpdateChangesStatusAndAddsToHistory() {
        val update = ShippingUpdate("created", "shipped", 1234567890L)
        shipment.addUpdate(update)

        assertEquals("shipped", shipment.status)
        assertEquals(listOf(update), shipment.getUpdateHistory())
    }

    @Test
    fun testSetExpectedDeliveryDate() {
        shipment.setExpectedDeliveryDate(9999999999)
        assertEquals(9999999999, shipment.expectedDeliveryDateTimestamp)
    }

    @Test
    fun testSetLocationUpdatesLocation() {
        shipment.setLocation("Chicago, IL")
        assertEquals("Chicago, IL", shipment.currentLocation)
    }

    @Test
    fun testObserverIsNotifiedOnNote() {
        var wasCalled = false
        val observer = object : org.kevinparks.shipmenttracker.observer.ShipmentObserver {
            override fun onShipmentUpdated(shipment: Shipment) {
                wasCalled = true
            }
        }

        shipment.registerObserver(observer)
        shipment.addNote("Test note")
        assertTrue(wasCalled)
    }

    @Test
    fun testObserverIsRemoved() {
        var callCount = 0
        val observer = object : ShipmentObserver {
            override fun onShipmentUpdated(shipment: Shipment) {
                callCount++
            }
        }

        shipment.registerObserver(observer)
        shipment.addNote("First")
        shipment.removeObserver(observer)
        shipment.addNote("Second")

        assertEquals(1, callCount) // ✅ only notified once
    }
}
