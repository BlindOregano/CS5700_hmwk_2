package org.kevinparks.shipmenttracker.server

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*
import org.junit.jupiter.api.Test
import org.kevinparks.shipmenttracker.server.startServer
import org.kevinparks.shipmenttracker.model.ShipmentRepository

class ServerTest {

    @Test
    fun `GET root should return HTML admin page`() = testApplication {
        application { startServer() }

        val response = client.get("/")

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("<h1>Shipment Admin Panel</h1>"))
    }

    @Test
    fun `POST update with created shipment should succeed and redirect`() = testApplication {
        application { startServer() }

        val response = client.post("/update") {
            setBody("created,test123,STANDARD,1722037200")
            header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
        }

        assertEquals(HttpStatusCode.Found, response.status) // Redirect
        assertNotNull(ShipmentRepository.getShipment("test123"))
    }

    @Test
    fun `GET shipment by id should return JSON`() = testApplication {
        application { startServer() }

        // Ensure a shipment exists
        client.post("/update") {
            setBody("created,abc999,STANDARD,1722037200")
            header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
        }

        val response = client.get("/shipment/abc999")

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("\"id\":\"abc999\""))
    }

    @Test
    fun `GET shipment with missing id should return bad request`() = testApplication {
        application { startServer() }

        val response = client.get("/shipment/")

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `POST update with unknown update type returns error`() = testApplication {
        application { startServer() }

        val response = client.post("/update") {
            setBody("flarp,test456,STANDARD,1722037200")
            header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertTrue(response.bodyAsText().contains("Unknown update type"))
    }
}
