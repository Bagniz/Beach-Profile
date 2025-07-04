package com.example.beachprofile.measures

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.beachprofile.common.ConfirmationDialogue
import com.example.beachprofile.sessions.FORMATTER

@Composable
fun MeasureItem(measure: Measure, measuresModel: MeasureViewModel) {
    var showConfirmationDialog = remember { mutableStateOf(false) }
    var isConfirmedDelete = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "θ %.2f°".format(measure.inclination),
                    style = MaterialTheme.typography.headlineSmall
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "λ %.4f°".format(measure.longitude),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "φ %.4f°".format(measure.latitude),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Surface(shape = RoundedCornerShape(5.dp)) {
                    Box(
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {

                        Text(
                            text = measure.note,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Justify
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = measure.timestamp.format(FORMATTER),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }

        IconButton(
            onClick = { showConfirmationDialog.value = true },
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.Red
            )
        }
    }

    if (showConfirmationDialog.value) {
        ConfirmationDialogue(showConfirmationDialog, isConfirmedDelete)
    }

    if (isConfirmedDelete.value) {
        measuresModel.deleteMeasureById(measure.id)
        isConfirmedDelete.value = false
    }
}