package org.kevinparks.shipmenttracker.simulator

import org.kevinparks.shipmenttracker.model.Shipment
import kotlin.test.*

class TrackingSimulatorTest {

    @Test
    fun testProcessLineCreatesShipmentAndAppliesUpdate() {
        val simulator = org.kevinparks.shipmenttracker.simulator.TrackingSimulator

        val line = "created,s9999,1234567890"
        val method = simulator::class.java.getDeclaredMethod("processLine", String::class.java)
        method.isAccessible = true
        method.invoke(simulator, line)

        val shipment = simulator.findShipment("s9999")
        assertNotNull(shipment)
        assertEquals("created", shipment!!.status)
    }

}