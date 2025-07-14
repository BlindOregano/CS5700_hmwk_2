package org.kevinparks.shipmenttracker.strategy

import org.kevinparks.shipmenttracker.model.Shipment
import org.kevinparks.shipmenttracker.model.ShippingUpdate

class LocationUpdateStrategy : UpdateStrategy {
    override fun applyUpdate(
        shipment: Shipment,
        args: List<String>,
        timestamp: Long): ShippingUpdate {

        if (args.isEmpty()) throw IllegalArgumentException("Missing location date")

        val newLocation = args[0]
        shipment.setLocation(newLocation)

        return ShippingUpdate(
            previousStatus = shipment.status,
            newStatus = shipment.status,         // status doesn't change
            timestamp = timestamp
        )
    }
}
