package com.example.beachprofile

import android.util.Log
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.beachprofile.models.Measure
import java.time.LocalDateTime

@Composable
fun AddMeasureForm(
    showAddMeasureDialog: MutableState<Boolean>,
    measures: MutableList<Measure>,
    inclination: MutableFloatState,
    latitude: MutableDoubleState,
    longitude: MutableDoubleState,
    startRegistering: () -> Unit,
    stopRegistering: () -> Unit
) {
    var transcription = remember { mutableStateOf("Press to start transcribing") }
    var saveLocationValues by remember { mutableStateOf(false) }

    Log.i(
        "${inclination.floatValue}, ${longitude.doubleValue}, ${latitude.doubleValue}",
        "ADD MEASURE"
    )

    Dialog(onDismissRequest = { showAddMeasureDialog.value = false }) {
        Surface(shape = RoundedCornerShape(16.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 2.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add measurement",
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(
                        text = "θ %.2f°".format(inclination.floatValue),
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Spacer(modifier = Modifier.weight(1f))
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
                if (!saveLocationValues) {
                    Button(onClick = {
                        saveLocationValues = true
                        stopRegistering()
                    }) { Text(text = "Save Location") }
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
                                saveLocationValues = false
                                startRegistering()
                            }) { Text(text = "Edit location") }
                            Spacer(modifier = Modifier.weight(1f))
                            Button(onClick = {
                                showAddMeasureDialog.value = false
                                measures.add(
                                    Measure(
                                        inclination.floatValue,
                                        longitude.doubleValue,
                                        latitude.doubleValue,
                                        transcription.value,
                                        LocalDateTime.now()
                                    )
                                )
                            }) { Text(text = "Save") }
                        }
                    }
                }
            }
        }
    }
}