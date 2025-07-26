package org.kevinparks.shipmenttracker.ui

import androidx.compose.runtime.*
import org.kevinparks.shipmenttracker.model.Shipment
import org.kevinparks.shipmenttracker.observer.ShipmentObserver
import org.kevinparks.shipmenttracker.simulator.TrackingSimulator
import java.text.SimpleDateFormat
import java.util.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import io.ktor.client.request.get

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

    var isAbnormal by mutableStateOf(false)
        private set

    var anomalyReason by mutableStateOf<String?>(null)
        private set

    private var currentShipment: Shipment? = null

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true})
        }
    }

    private var pollingJob: Job? = null

    fun trackShipment(id: String) {
        stopTracking()

        shipmentId = id
        shipmentStatus = "Loading. . . "

        pollingJob = CoroutineScope(Dispatchers.IO).launch {
            while(isActive) {
                try {
                    val shipment: Shipment = client.get("http://localhost:8080/shipment/$id").body()

                    withContext(Dispatchers.Main) {
                        currentShipment = shipment
                        updateStateFromShipment(shipment)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        shipmentStatus = "Shipment not found"
                        shipmentLocation = ""
                        shipmentNotes = emptyList()
                        shipmentUpdateHistory = emptyList()
                        expectedShipmentDeliveryDate = ""
                    }
                }
                delay(2000L)
            }
        }
    }

    fun stopTracking() {
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
        isAbnormal = shipment.isAbnormal
        anomalyReason = shipment.anomalyReason

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