package org.kevinparks.shipmenttracker.model

import kotlinx.serialization.Serializable

@Serializable
enum class ShipmentType {
    EXPRESS, STANDARD, OVERNIGHT, BULK
}