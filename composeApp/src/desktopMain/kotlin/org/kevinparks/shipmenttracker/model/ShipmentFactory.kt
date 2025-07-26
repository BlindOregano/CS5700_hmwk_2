package org.kevinparks.shipmenttracker.model

object ShipmentFactory {

    fun createShipment(
        id: String,
        type: ShipmentType,
        createdAt: Long
    ): Shipment {
        return Shipment(id, createdAt, type)
    }

    fun parseType(typeStr: String): ShipmentType {
        return when (typeStr.uppercase()) {
            "EXPRESS" -> ShipmentType.EXPRESS
            "STANDARD" -> ShipmentType.STANDARD
            "OVERNIGHT" -> ShipmentType.OVERNIGHT
            "BULK" -> ShipmentType.BULK
            else -> throw IllegalArgumentException("Unknown ShipmentType: $typeStr")
        }
    }
}