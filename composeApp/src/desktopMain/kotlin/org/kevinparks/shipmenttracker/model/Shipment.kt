package org.kevinparks.shipmenttracker.model

import org.kevinparks.shipmenttracker.observer.ShipmentObserver

class Shipment(
    val id: String,
) {
    var status: String = "created"
        private set

    private val notes = mutableListOf<String>()
    private val updateHistory = mutableListOf<ShippingUpdate>()
    var expectedDeliveryDateTimestamp: Long = 0L
        private set
    var currentLocation: String = ""
        private set

    private val observers = mutableListOf<ShipmentObserver>()

    fun addNote(note: String) {
        notes.add(note)
        notifyObservers()
    }

    fun addUpdate(update: ShippingUpdate) {
        updateHistory.add(update)
        status = update.newStatus
        notifyObservers()
    }

    fun setExpectedDeliveryDate(timestamp: Long) {
        expectedDeliveryDateTimestamp = timestamp
        notifyObservers()
    }

    fun setLocation(location: String) {
        currentLocation = location
        notifyObservers()
    }

    fun registerObserver(observer: ShipmentObserver) {
        observers.add(observer)
    }

    fun removeObserver(observer: ShipmentObserver){
        observers.remove(observer)
    }

    private fun notifyObservers() {
        observers.forEach { it.onShipmentUpdated(this)}
    }

    fun getNotes(): List<String> = notes.toList()
    fun getUpdateHistory(): List<ShippingUpdate> = updateHistory.toList()

}