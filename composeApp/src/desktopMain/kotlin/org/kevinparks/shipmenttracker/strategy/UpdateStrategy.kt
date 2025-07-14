package org.kevinparks.shipmenttracker.strategy

import org.kevinparks.shipmenttracker.model.Shipment
import org.kevinparks.shipmenttracker.model.ShippingUpdate

interface UpdateStrategy {
    fun applyUpdate(
        shipment: Shipment,
        args: List<String>,
        timestamp: Long): ShippingUpdate
}