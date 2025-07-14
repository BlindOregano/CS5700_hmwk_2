package org.kevinparks.shipmenttracker.strategy

import org.kevinparks.shipmenttracker.model.Shipment
import org.kevinparks.shipmenttracker.model.ShippingUpdate

class NoteAddedUpdateStrategy : UpdateStrategy {
    override fun applyUpdate(
        shipment: Shipment,
        args: List<String>,
        timestamp: Long): ShippingUpdate {

        if (args.isEmpty()) throw IllegalArgumentException("Missing note text")

        shipment.addNote(args[0])

        return ShippingUpdate(
            previousStatus = shipment.status,
            newStatus = shipment.status,       //Status doesn't change
            timestamp = timestamp
        )
    }
}
