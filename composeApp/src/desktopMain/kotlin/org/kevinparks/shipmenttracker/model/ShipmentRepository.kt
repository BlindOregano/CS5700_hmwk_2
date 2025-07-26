package org.kevinparks.shipmenttracker.model

object ShipmentRepository {
    private val shipments = mutableMapOf<String, Shipment>()

    fun addShipment(shipment: Shipment) {
        shipments[shipment.id] = shipment
    }

    fun getShipment(id: String): Shipment? {
        return shipments[id]
    }

    fun contains(id: String): Boolean {
        return shipments.containsKey(id)
    }

    fun getAllShipments(): List<Shipment> {
        return shipments.values.toList()
    }

    fun clearAll() {
        shipments.clear()
    }
}