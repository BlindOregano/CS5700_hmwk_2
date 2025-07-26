package org.kevinparks.shipmenttracker

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.kevinparks.shipmenttracker.simulator.TrackingSimulator
import org.kevinparks.shipmenttracker.ui.MainScreen

fun main() = application {
    println("1")
    org.kevinparks.shipmenttracker.server.startServer()
    println("2")
//    TrackingSimulator.runSimulation()
    println("3")
    Window(onCloseRequest = ::exitApplication, title = "ShipmentTracker") {
        println("4")
        MainScreen()
    }
    println("5")
}
