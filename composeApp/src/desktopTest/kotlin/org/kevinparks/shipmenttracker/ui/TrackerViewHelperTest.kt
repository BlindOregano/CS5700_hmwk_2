package org.kevinparks.shipmenttracker.ui

import org.kevinparks.shipmenttracker.model.Shipment
import org.kevinparks.shipmenttracker.model.ShippingUpdate
import kotlin.test.*

class TrackerViewHelperTest {

    @Test
    fun testViewHelperTracksShipmentAndUpdatesState() {
        val helper = TrackerViewHelper()
        val shipment = Shipment("s8888")

        org.kevinparks.shipmenttracker.simulator.TrackingSimulator.addShipment(shipment)
        helper.trackShipment("s8888")

        shipment.setLocation("Houston")
        shipment.setExpectedDeliveryDate(1000000)
        shipment.addNote("Rush delivery")
        shipment.addUpdate(ShippingUpdate("created", "shipped", 123))

        assertEquals("shipped", helper.shipmentStatus)
        assertTrue(helper.shipmentNotes.contains("Rush delivery"))
    }

}