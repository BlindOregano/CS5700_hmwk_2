package org.kevinparks.shipmenttracker.observer

interface Subject {
    fun registerObserver(observer: ShipmentObserver)
    fun removeObserver(observer: ShipmentObserver)
    fun notifyObservers()
}