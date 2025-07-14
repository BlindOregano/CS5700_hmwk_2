package org.kevinparks.shipmenttracker.observer

import org.kevinparks.shipmenttracker.model.Shipment

interface ShipmentObserver {
    fun onShipmentUpdated(shipment: Shipment)
}