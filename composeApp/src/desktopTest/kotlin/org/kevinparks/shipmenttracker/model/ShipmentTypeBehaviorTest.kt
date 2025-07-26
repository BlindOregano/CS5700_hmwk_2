package org.kevinparks.shipmenttracker.model

import org.junit.jupiter.api.Test
import org.kevinparks.shipmenttracker.model.ShipmentTypeBehavior
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ShipmentTypeBehaviorTest {

    private val baseTimestamp = 1_000_000L

    @Test
    fun `Standard behavior always returns null`() {
        val result = ShipmentTypeBehavior.Standard.validateDeliveryDate(baseTimestamp, baseTimestamp + 10_000)
        assertNull(result)
    }

    @Test
    fun `Express shipment within 3 days passes validation`() {
        val threeDays = 3 * 24 * 60 * 60L
        val result = ShipmentTypeBehavior.Express.validateDeliveryDate(baseTimestamp, baseTimestamp + threeDays)
        assertNull(result)
    }

    @Test
    fun `Express shipment beyond 3 days returns error`() {
        val tooLate = baseTimestamp + (3 * 24 * 60 * 60L) + 1
        val result = ShipmentTypeBehavior.Express.validateDeliveryDate(baseTimestamp, tooLate)
        assertEquals(
            "An express shipment was updated with a delivery date more than 3 days after creation.",
            result
        )
    }

    @Test
    fun `Overnight shipment within 1 day passes validation`() {
        val oneDay = 24 * 60 * 60L
        val result = ShipmentTypeBehavior.Overnight.validateDeliveryDate(baseTimestamp, baseTimestamp + oneDay)
        assertNull(result)
    }

    @Test
    fun `Overnight shipment beyond 1 day returns error`() {
        val tooLate = baseTimestamp + (24 * 60 * 60L) + 1
        val result = ShipmentTypeBehavior.Overnight.validateDeliveryDate(baseTimestamp, tooLate)
        assertEquals(
            "An overnight shipment was updated with a delivery date later than 24 hours after it was created.",
            result
        )
    }

    @Test
    fun `Bulk shipment with delay of 3+ days passes validation`() {
        val threeDays = 3 * 24 * 60 * 60L
        val result = ShipmentTypeBehavior.Bulk.validateDeliveryDate(baseTimestamp, baseTimestamp + threeDays)
        assertNull(result)
    }

    @Test
    fun `Bulk shipment with delay less than 3 days returns error`() {
        val tooSoon = baseTimestamp + (3 * 24 * 60 * 60L) - 1
        val result = ShipmentTypeBehavior.Bulk.validateDeliveryDate(baseTimestamp, tooSoon)
        assertEquals(
            "A bulk shipment was updated with a delivery date sooner than 3 days after it was created.",
            result
        )
    }

    @Test
    fun `All behaviors return null if expectedDeliveryDate is null`() {
        val behaviors = listOf(
            ShipmentTypeBehavior.Standard,
            ShipmentTypeBehavior.Express,
            ShipmentTypeBehavior.Overnight,
            ShipmentTypeBehavior.Bulk
        )

        behaviors.forEach { behavior ->
            val result = behavior.validateDeliveryDate(baseTimestamp, null)
            assertNull(result)
        }
    }
}
