package org.kevinparks.shipmenttracker.observer

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.kevinparks.shipmenttracker.model.Shipment
import org.kevinparks.shipmenttracker.model.ShipmentType
import org.kevinparks.shipmenttracker.observer.ShipmentObserver

class ShipmentObserverTest {

    @Test
    fun `ShipmentObserver is notified on shipment update`() {
        val shipment = Shipment("OBS001", 1000L, ShipmentType.STANDARD)
        val observer = mockk<ShipmentObserver>(relaxed = true)

        shipment.registerObserver(observer)
        shipment.addNote("Observer test note")

        verify { observer.onShipmentUpdated(shipment) }
    }
}
