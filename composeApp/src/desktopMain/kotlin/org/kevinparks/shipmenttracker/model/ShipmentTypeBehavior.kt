package org.kevinparks.shipmenttracker.model

import kotlin.math.abs

sealed class ShipmentTypeBehavior {
    abstract fun validateDeliveryDate(createdAt: Long, expectedDeliveryDate: Long?): String?

    object Standard : ShipmentTypeBehavior() {
        override fun validateDeliveryDate(createdAt: Long, expectedDeliveryDate: Long?) = null
    }

    object Express : ShipmentTypeBehavior() {
        override fun validateDeliveryDate(createdAt: Long, expectedDeliveryDate: Long?): String? {
            val maxDelay = 3 * 24 * 60 * 60 // 3 days in seconds

            if (expectedDeliveryDate != null && createdAt != null) {
                println("Expected - Created: ${expectedDeliveryDate - createdAt}")
            }
            return if (expectedDeliveryDate != null && abs(expectedDeliveryDate - createdAt) > maxDelay)
                "An express shipment was updated with a delivery date more than 3 days after creation."
            else null
        }
    }

    object Overnight : ShipmentTypeBehavior() {
        override fun validateDeliveryDate(createdAt: Long, expectedDeliveryDate: Long?): String? {
            val maxDelay = 24 * 60 * 60 // 1 day in seconds
            return if (expectedDeliveryDate != null && expectedDeliveryDate - createdAt > maxDelay)
                "An overnight shipment was updated with a delivery date later than 24 hours after it was created."
            else null
        }
    }

    object Bulk : ShipmentTypeBehavior() {
        override fun validateDeliveryDate(createdAt: Long, expectedDeliveryDate: Long?): String? {
            val minDelay = 3 * 24 * 60 * 60 // 3 days in seconds
            return if (expectedDeliveryDate != null && expectedDeliveryDate - createdAt < minDelay)
                "A bulk shipment was updated with a delivery date sooner than 3 days after it was created."
            else null
        }
    }
}


fun shipmentTypeBehaviorFor(type: ShipmentType): ShipmentTypeBehavior = when (type) {
    ShipmentType.STANDARD -> ShipmentTypeBehavior.Standard
    ShipmentType.EXPRESS -> ShipmentTypeBehavior.Express
    ShipmentType.OVERNIGHT -> ShipmentTypeBehavior.Overnight
    ShipmentType.BULK -> ShipmentTypeBehavior.Bulk
}
