package org.kevinparks.shipmenttracker.strategy

import org.kevinparks.shipmenttracker.model.Shipment
import org.kevinparks.shipmenttracker.model.ShippingUpdate

class DeliveredUpdateStrategy : UpdateStrategy {
    override fun applyUpdate(
        shipment: Shipment,
        args: List<String>,
        timestamp: Long): ShippingUpdate {

        return ShippingUpdate(
            previousStatus = shipment.status,
            newStatus = "delivered",
            timestamp = timestamp
        )
    }
}
