package org.kevinparks.shipmenttracker.strategy

import org.kevinparks.shipmenttracker.model.Shipment
import org.kevinparks.shipmenttracker.model.ShippingUpdate

class ShippedUpdateStrategy : UpdateStrategy {
    override fun applyUpdate(
        shipment: Shipment,
        args: List<String>,
        timestamp: Long): ShippingUpdate {

        if (args.isEmpty()) throw IllegalArgumentException("Missing expected delivery date")

        val expectedTimestamp = args[0].toLong()
        shipment.setExpectedDeliveryDate(expectedTimestamp)
        shipment.checkForAnomalies()

        return ShippingUpdate(
            previousStatus = shipment.status,
            newStatus = "shipped",
            timestamp = timestamp
        )
    }
}
