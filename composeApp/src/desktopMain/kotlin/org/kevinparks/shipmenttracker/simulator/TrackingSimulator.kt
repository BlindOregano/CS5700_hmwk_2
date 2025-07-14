package org.kevinparks.shipmenttracker.simulator

import org.kevinparks.shipmenttracker.model.Shipment
import org.kevinparks.shipmenttracker.strategy.*
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object TrackingSimulator {

    private val shipments = mutableMapOf<String, Shipment>()

    private val updateStrategies = mapOf(
        "created" to CreatedUpdateStrategy(),
        "shipped" to ShippedUpdateStrategy(),
        "location" to LocationUpdateStrategy(),
        "delivered" to DeliveredUpdateStrategy(),
        "delayed" to DelayedUpdateStrategy(),
        "lost" to LostUpdateStrategy(),
        "canceled" to CanceledUpdateStrategy(),
        "noteadded" to NoteAddedUpdateStrategy()
    )

    fun findShipment(id: String): Shipment? = shipments[id]

    fun addShipment(shipment: Shipment) {
        shipments[shipment.id] = shipment
    }


    fun runSimulation(resourcePath: String = "test.txt") {
        GlobalScope.launch {
            val inputStream = this::class.java.classLoader.getResourceAsStream(resourcePath)
                ?: throw IllegalArgumentException("Could not load resource: $resourcePath")

            val reader = inputStream.bufferedReader()
            var line: String? = reader.readLine()

            while (line != null) {
                try {
                    processLine(line)
                } catch (e: Exception) {
                    println("Error processing line: $line\n${e.message}")
                }

                delay(1000L)
                line = reader.readLine()
            }
        }
    }

    private fun processLine(line: String) {
        val parts = line.split(",", limit = 4)

        val updateType = parts.getOrNull(0)?.lowercase() ?: return
        val shipmentId = parts.getOrNull(1) ?: return
        val timestamp = parts.getOrNull(2)?.toLongOrNull() ?: return
        val otherInfo = parts.getOrNull(3)?.let { listOf(it) } ?: emptyList()

        val strategy = updateStrategies[updateType] ?: throw IllegalStateException("Unknown update type: $updateType")

        val shipment = shipments.getOrPut(shipmentId) { Shipment(shipmentId) }

        val update = strategy.applyUpdate(shipment, otherInfo, timestamp)
        shipment.addUpdate(update)
    }

}