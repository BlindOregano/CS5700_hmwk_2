package org.kevinparks.shipmenttracker.simulator
import kotlinx.coroutines.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.kevinparks.shipmenttracker.simulator.TrackingSimulator
import org.kevinparks.shipmenttracker.model.TrackerViewHelper
import org.kevinparks.shipmenttracker.model.Shipment
import org.kevinparks.shipmenttracker.ui.TrackerViewHelper

class TrackingSimulatorTests {

    private val testHelper = TrackerViewHelper()

    init {
        TrackingSimulator.setHelper(testHelper)
    }

    @Test
    fun testProcessCreatedLine() = runBlocking {
        val line = "created,1001,1000000"
        TrackingSimulator.processLine(line)
        val shipment = testHelper.getShipment("1001")
        assertNotNull(shipment)
        assertEquals("created", shipment?.status)
    }

    @Test
    fun testProcessShippedLine() = runBlocking {
        TrackingSimulator.processLine("created,1002,1000000")
        TrackingSimulator.processLine("shipped,1002,1000001,1005000")
        val shipment = testHelper.getShipment("1002")
        assertEquals("shipped", shipment?.status)
        assertEquals(1005000L, shipment?.expectedDelivery)
    }

    @Test
    fun testProcessLocationLine() = runBlocking {
        TrackingSimulator.processLine("created,1003,1000000")
        TrackingSimulator.processLine("location,1003,1000002,Seattle WA")
        val shipment = testHelper.getShipment("1003")
        assertEquals("Seattle WA", shipment?.location)
    }

    @Test
    fun testProcessNoteAddedLine() = runBlocking {
        TrackingSimulator.processLine("created,1004,1000000")
        TrackingSimulator.processLine("noteadded,1004,1000003,Box was dented")
        val shipment = testHelper.getShipment("1004")
        assertTrue(shipment?.notes?.contains("Box was dented") == true)
    }

    @Test
    fun testProcessDeliveredLine() = runBlocking {
        TrackingSimulator.processLine("created,1005,1000000")
        TrackingSimulator.processLine("delivered,1005,1000004")
        val shipment = testHelper.getShipment("1005")
        assertEquals("delivered", shipment?.status)
    }

    @Test
    fun testProcessLostLine() = runBlocking {
        TrackingSimulator.processLine("created,1006,1000000")
        TrackingSimulator.processLine("lost,1006,1000005")
        val shipment = testHelper.getShipment("1006")
        assertEquals("lost", shipment?.status)
    }

    @Test
    fun testProcessCanceledLine() = runBlocking {
        TrackingSimulator.processLine("created,1007,1000000")
        TrackingSimulator.processLine("canceled,1007,1000006")
        val shipment = testHelper.getShipment("1007")
        assertEquals("canceled", shipment?.status)
    }

    @Test
    fun testProcessDelayedLine() = runBlocking {
        TrackingSimulator.processLine("created,1008,1000000")
        TrackingSimulator.processLine("delayed,1008,1000007,1006000")
        val shipment = testHelper.getShipment("1008")
        assertEquals("delayed", shipment?.status)
        assertEquals(1006000L, shipment?.expectedDelivery)
    }
}
