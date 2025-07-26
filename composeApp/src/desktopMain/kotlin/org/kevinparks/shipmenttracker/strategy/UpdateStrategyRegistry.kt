package org.kevinparks.shipmenttracker.strategy

object UpdateStrategyRegistry {
    private val strategies = mapOf(
        "created" to CreatedUpdateStrategy(),
        "shipped" to ShippedUpdateStrategy(),
        "location" to LocationUpdateStrategy(),
        "delivered" to DeliveredUpdateStrategy(),
        "delayed" to DelayedUpdateStrategy(),
        "lost" to LostUpdateStrategy(),
        "canceled" to CanceledUpdateStrategy(),
        "noteadded" to NoteAddedUpdateStrategy()
    )

    fun getStrategy(type: String): UpdateStrategy? {
        return strategies[type.lowercase()]
    }
}