package org.kevinparks.shipmenttracker.model

import kotlinx.serialization.Contextual
import org.kevinparks.shipmenttracker.observer.ShipmentObserver
import org.kevinparks.shipmenttracker.observer.Subject
import kotlinx.serialization.Serializable

@Serializable
data class Shipment(
    val id: String,
    val createdAt: Long,
    val shipmentType: ShipmentType
) : Subject {
    var status: String = "created"
        private set

    private val notes = mutableListOf<String>()
    private val updateHistory = mutableListOf<ShippingUpdate>()
    var expectedDeliveryDateTimestamp: Long = 0L
        private set
    var currentLocation: String = ""
        private set

    var isAbnormal: Boolean = false
    var anomalyReason: String? = null

   val behavior: ShipmentTypeBehavior by lazy { shipmentTypeBehaviorFor(shipmentType) }

    @Transient
    private val observers = mutableListOf<ShipmentObserver>()

    fun addNote(note: String) {
        notes.add(note)
        notifyObservers()
        checkForAnomalies()
    }

    fun addUpdate(update: ShippingUpdate) {
        updateHistory.add(update)
        status = update.newStatus
        notifyObservers()
        checkForAnomalies()
    }

    fun setExpectedDeliveryDate(timestamp: Long) {
        expectedDeliveryDateTimestamp = timestamp
        notifyObservers()
        checkForAnomalies()
    }

    fun setLocation(location: String) {
        currentLocation = location
        notifyObservers()
    }

    override fun registerObserver(observer: ShipmentObserver) {
        observers.add(observer)
    }

    override fun removeObserver(observer: ShipmentObserver){
        observers.remove(observer)
    }

    override fun notifyObservers() {
        observers.forEach { it.onShipmentUpdated(this)}
    }

    fun getNotes(): List<String> = notes.toList()
    fun getUpdateHistory(): List<ShippingUpdate> = updateHistory.toList()

    fun checkForAnomalies() {
        // Only validate if we have an expected delivery date
        if (expectedDeliveryDateTimestamp != 0L) {
            val reason = behavior.validateDeliveryDate(createdAt, expectedDeliveryDateTimestamp)
            println(reason)
            if (reason != null) {
                println("Should be working")
                isAbnormal = true
                anomalyReason = reason
                notifyObservers()
            } else {
                isAbnormal = false
                anomalyReason = null
            }
        }
    }
}