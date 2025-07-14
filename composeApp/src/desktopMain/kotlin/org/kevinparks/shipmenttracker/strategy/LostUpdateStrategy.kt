package org.kevinparks.shipmenttracker.strategy

import org.kevinparks.shipmenttracker.model.Shipment
import org.kevinparks.shipmenttracker.model.ShippingUpdate

class LostUpdateStrategy : UpdateStrategy {
    override fun applyUpdate(
        shipment: Shipment,
        args: List<String>,
        timestamp: Long): ShippingUpdate {

        return ShippingUpdate(
            previousStatus = shipment.status,
            newStatus = "lost",
            timestamp = timestamp
        )
    }
}