package org.kevinparks.shipmenttracker.model

data class ShippingUpdate(
    val previousStatus: String,
    val newStatus: String,
    val timestamp: Long
) { }