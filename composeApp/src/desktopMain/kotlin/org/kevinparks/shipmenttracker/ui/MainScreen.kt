package org.kevinparks.shipmenttracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun MainScreen() {
    var inputId by remember {mutableStateOf("")}
    val trackedHelpers = remember { mutableStateListOf<TrackerViewHelper>()}

    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextField(
                value = inputId,
                onValueChange = { inputId = it },
                label = { Text("Shipment ID") },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                if (inputId.isNotBlank()) {
                    val helper = TrackerViewHelper()
                    helper.trackShipment(inputId.trim())
                    trackedHelpers.add(helper)
                    inputId = ""
                }
            }) {
                Text("Track")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(trackedHelpers.size) { index ->
                val helper = trackedHelpers[index]
                ShipmentCard(helper) {
                    helper.stopTracking()
                    trackedHelpers.remove(helper)
                }
            }
        }
    }
}