package com.example.beachprofile

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.beachprofile.models.Session
import java.time.LocalDateTime

@Composable
fun AddSessionForm(showAddSessionForm: MutableState<Boolean>, sessions: MutableList<Session>) {
    var context = LocalContext.current
    var sessionName by remember { mutableStateOf<String>("") }
    var sessionDate by remember { mutableStateOf<LocalDateTime>(LocalDateTime.now()) }
    Dialog(onDismissRequest = { showAddSessionForm.value = false }) {
        Surface(shape = RoundedCornerShape(16.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Add a new session", style = MaterialTheme.typography.headlineMedium)
                TextField(
                    value = sessionName,
                    onValueChange = { sessionName = it },
                    enabled = true,
                    label = { Text("Session name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = { showAddSessionForm.value = false },
                    ) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = {
                        if (sessionName.isNotBlank()) {
                            sessionDate = LocalDateTime.now()
                            // TODO: Change line below
                            sessions.add(Session(sessionName, sessionDate))
                            Toast.makeText(
                                context,
                                "Session: $sessionName created",
                                Toast.LENGTH_SHORT
                            ).show()
                            showAddSessionForm.value = false
                        } else {
                            Toast.makeText(context, "Please fill session name", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}