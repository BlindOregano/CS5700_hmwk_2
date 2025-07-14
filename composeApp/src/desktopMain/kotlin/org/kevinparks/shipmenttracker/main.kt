package org.kevinparks.shipmenttracker

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.kevinparks.shipmenttracker.simulator.TrackingSimulator
import org.kevinparks.shipmenttracker.ui.MainScreen

fun main() = application {
    TrackingSimulator.runSimulation()
    Window(onCloseRequest = ::exitApplication, title = "ShipmentTracker") {
        MainScreen()
    }
}
