package org.kevinparks.shipmenttracker.model
import kotlinx.serialization.Serializable

@Serializable
data class ShippingUpdate(
    val previousStatus: String,
    val newStatus: String,
    val timestamp: Long
) { }