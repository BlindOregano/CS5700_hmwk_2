package org.kevinparks.shipmenttracker.strategy

import org.kevinparks.shipmenttracker.model.Shipment
import org.kevinparks.shipmenttracker.model.ShippingUpdate

class DelayedUpdateStrategy : UpdateStrategy {
    override fun applyUpdate(
        shipment: Shipment,
        args: List<String>,
        timestamp: Long): ShippingUpdate {

        if (args.isEmpty()) throw IllegalArgumentException("Missing new expected delivery date")

        val newExpected = args[0].toLong()
        shipment.setExpectedDeliveryDate(newExpected)

        return ShippingUpdate(
            previousStatus = shipment.status,
            newStatus = "delayed",
            timestamp = timestamp
        )
    }
}
