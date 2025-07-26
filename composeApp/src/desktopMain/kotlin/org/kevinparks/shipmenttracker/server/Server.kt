package org.kevinparks.shipmenttracker.server

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.kevinparks.shipmenttracker.model.ShipmentFactory
import org.kevinparks.shipmenttracker.model.ShipmentRepository
import org.kevinparks.shipmenttracker.strategy.UpdateStrategyRegistry
import kotlinx.serialization.Serializable

fun startServer() {
    embeddedServer(Netty, port = 8080) {
        install(CallLogging)
        install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
            json()
        }
        routing {
            get("/") {
                call.respondText(
                    """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Shipment Admin</title>
        </head>
        <body>
            <h1>Shipment Admin Panel</h1>
            
            <h2>Create or Update Shipment</h2>
            <form action="/update" method="post">
                <label>Update Command:</label><br>
                <input type="text" name="updateCommand" value="created,s100,STANDARD,1722037200" size="60">
                <br><br>
                <button type="submit">Submit</button>
            </form>
        </body>
        </html>
        """.trimIndent(),
                    ContentType.Text.Html
                )
            }
            post("/update") {
                val input = call.receiveParameters()["updateCommand"] ?: call.receiveText()
//                val input = call.receiveText()
                val fields = input.split(",").map { it.trim() }

                if (fields.isEmpty()){
                    call.respond(HttpStatusCode.BadRequest, "Empty input")
                    return@post
                }

                val updateType = fields[0].lowercase()
                val shipmentId = fields.getOrNull(1) ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing shipment ID")

                val strategy = UpdateStrategyRegistry.getStrategy(updateType) ?: return@post call.respond(HttpStatusCode.BadRequest, "Unknown update type $updateType")

                val timestamp = System.currentTimeMillis()

                if (updateType == "created") {
                    val typeStr = fields.getOrNull(2) ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing shipment type")
                    val createdAt = fields.getOrNull(3)?.toLongOrNull() ?: return@post call.respond(HttpStatusCode.BadRequest, "Invalid or missing created time")

                    if (!ShipmentRepository.contains(shipmentId)) {
                        val shipmentType = ShipmentFactory.parseType(typeStr)
                        val shipment = ShipmentFactory.createShipment(shipmentId, shipmentType, createdAt)
                        ShipmentRepository.addShipment(shipment)
                    }

                    val shipment = ShipmentRepository.getShipment(shipmentId)
                    val update = strategy.applyUpdate(shipment!!, fields.drop(2), timestamp)
                    shipment.addUpdate(update)
                } else {
                    val shipment = ShipmentRepository.getShipment(shipmentId) ?: return@post call.respond(HttpStatusCode.NotFound, "Shipment not found: $shipmentId")

                    val update = strategy.applyUpdate(shipment, fields.drop(2), timestamp)
                    shipment.addUpdate(update)
                }

                call.respondRedirect("/")

            }
            get("/shipment/{id}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Missing shipment ID")
                    return@get
                }

                val shipment = ShipmentRepository.getShipment(id)
                if (shipment == null) {
                    call.respond(HttpStatusCode.NotFound, "Shipment not found: $id")
                } else {
                    call.respond(shipment)
                }
            }
        }
    }.start(wait = false) // <- Don't block the main thread!
}
