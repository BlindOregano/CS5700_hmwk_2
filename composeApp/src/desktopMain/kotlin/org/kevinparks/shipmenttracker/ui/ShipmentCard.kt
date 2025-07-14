package org.kevinparks.shipmenttracker.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun ShipmentCard(helper: TrackerViewHelper, onStopTracking: () -> Unit) {
    var historyExpanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
                Text("ID: ${helper.shipmentId}", style = MaterialTheme.typography.h6)
                Button(onClick = onStopTracking) {
                    Text("Stop")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Status: ${helper.shipmentStatus}")
            Text("Location: ${helper.shipmentLocation}")
            if (helper.expectedShipmentDeliveryDate.isNotBlank()) {
                Text("Expected Delivery: ${helper.expectedShipmentDeliveryDate}")
            }

            if (helper.shipmentNotes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Notes:")
                helper.shipmentNotes.forEach {
                    Text("- $it", style = MaterialTheme.typography.body2)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {historyExpanded = !historyExpanded}) {
                Text(if (historyExpanded) "Hide Updates" else "Show Updates")
            }

            AnimatedVisibility(visible = historyExpanded) {
                Column {
                    helper.shipmentUpdateHistory.forEach {
                        Text("- $it", style = MaterialTheme.typography.body2)
                    }
                }
            }
        }
    }
}