package org.kevinparks.shipmenttracker.ui

import androidx.compose.runtime.*
import org.kevinparks.shipmenttracker.model.Shipment
import org.kevinparks.shipmenttracker.observer.ShipmentObserver
import org.kevinparks.shipmenttracker.simulator.TrackingSimulator
import java.text.SimpleDateFormat
import java.util.*

class TrackerViewHelper : ShipmentObserver {
    var shipmentId by mutableStateOf("")
        private set

    var shipmentStatus by mutableStateOf("")
        private set

    var shipmentLocation by mutableStateOf("")
        private set

    var shipmentNotes by mutableStateOf(listOf<String>())
        private set

    var shipmentUpdateHistory by mutableStateOf(listOf<String>())
        private set

    var expectedShipmentDeliveryDate by mutableStateOf("")
        private set

    private var currentShipment: Shipment? = null

    fun trackShipment(id: String) {
        val shipment = TrackingSimulator.findShipment(id)
        if (shipment == null) {
            // Set some "not found" message for UI to display
            shipmentId = id
            shipmentStatus = "Shipment not found"
            shipmentLocation = ""
            shipmentNotes = emptyList()
            shipmentUpdateHistory = emptyList()
            expectedShipmentDeliveryDate = ""
            currentShipment = null
            return
        }

        stopTracking()
        shipment.registerObserver(this)

        shipmentId = shipment.id
        currentShipment = shipment
        updateStateFromShipment(shipment)
    }

    fun stopTracking() {
        currentShipment?.removeObserver(this)
        currentShipment = null
    }

    override fun onShipmentUpdated(shipment: Shipment) {
        updateStateFromShipment(shipment)
    }

    private fun updateStateFromShipment(shipment: Shipment) {
        shipmentStatus = shipment.status
        shipmentLocation = shipment.currentLocation
        shipmentNotes = shipment.getNotes()
        expectedShipmentDeliveryDate = formatTimestamp(shipment.expectedDeliveryDateTimestamp)

        shipmentUpdateHistory = shipment.getUpdateHistory().map {
            "Shipment went from ${it.previousStatus} to ${it.newStatus} on ${formatTimestamp(it.timestamp)}"
        }
    }

    private fun formatTimestamp(timestamp: Long): String {
        if (timestamp == 0L) return ""
        val date = Date(timestamp)
        val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(date)
    }

}