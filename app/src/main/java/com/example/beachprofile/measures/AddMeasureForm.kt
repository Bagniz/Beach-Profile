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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Composable
fun AddMeasureForm(
    sessionId: Int,
    measureModel: MeasureViewModel,
    showAddMeasureDialog: MutableState<Boolean>,
    inclination: MutableFloatState,
    latitude: MutableDoubleState,
    longitude: MutableDoubleState,
    startRegistering: () -> Unit,
    stopRegistering: () -> Unit,
    scrollDown: () -> Unit
) {
    var transcription = remember { mutableStateOf("Press to start transcribing") }
    var addTranscribedNote by remember { mutableStateOf(false) }

    startRegistering()

    Dialog(onDismissRequest = { showAddMeasureDialog.value = false }) {
        Surface(shape = RoundedCornerShape(16.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add measurement",
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "θ %.2f°".format(inclination.floatValue),
                    style = MaterialTheme.typography.headlineSmall,
                )
                Row {
                    Text(
                        text = "λ %.4f°".format(longitude.doubleValue),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "φ %.4f°".format(latitude.doubleValue),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                if (!addTranscribedNote) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        Button(onClick = {
                            addTranscribedNote = true
                            stopRegistering()
                        }) { Text(text = "Add note") }
                        Button(onClick = {
                            val newMeasure = Measure(
                                sessionId = sessionId,
                                inclination = inclination.floatValue,
                                longitude = longitude.doubleValue,
                                latitude = latitude.doubleValue,
                                note = "No note added",
                                timestamp = LocalDateTime.now()
                            )
                            measureModel.addMeasure(newMeasure)
                            scrollDown()
                        }) {Text("Save location")}
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            shape = RoundedCornerShape(5.dp),
                            modifier = Modifier.padding(horizontal = 5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {

                                Text(
                                    text = transcription.value,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Justify
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        VoiceTranscriber(transcription)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row {
                            Button(onClick = {
                                addTranscribedNote = false
                                startRegistering()
                            }) { Text(text = "Edit location") }
                            Spacer(modifier = Modifier.weight(1f))
                            Button(onClick = {
                                showAddMeasureDialog.value = false
                                val newMeasure = Measure(
                                    sessionId = sessionId,
                                    inclination = inclination.floatValue,
                                    longitude = longitude.doubleValue,
                                    latitude = latitude.doubleValue,
                                    note = transcription.value,
                                    timestamp = LocalDateTime.now()
                                )
                                measureModel.addMeasure(newMeasure)
                                scrollDown()
                            }) { Text(text = "Save") }
                        }
                    }
                }
            }
        }
    }
}