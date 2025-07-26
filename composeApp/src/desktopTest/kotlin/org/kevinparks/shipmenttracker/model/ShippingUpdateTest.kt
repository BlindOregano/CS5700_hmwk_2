package org.kevinparks.shipmenttracker.model

import org.junit.jupiter.api.Test
import org.kevinparks.shipmenttracker.model.ShippingUpdate
import kotlin.test.assertEquals

class ShippingUpdateTest {

    @Test
    fun `ShippingUpdate stores values correctly`() {
        val update = ShippingUpdate(
            previousStatus = "created",
            newStatus = "shipped",
            timestamp = 123456789L
        )

        assertEquals("created", update.previousStatus)
        assertEquals("shipped", update.newStatus)
        assertEquals(123456789L, update.timestamp)
    }
}
