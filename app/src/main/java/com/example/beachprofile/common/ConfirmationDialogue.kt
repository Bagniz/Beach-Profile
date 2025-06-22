package com.example.beachprofile.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun ConfirmationDialogue(
    confirmationDialog: MutableState<Boolean>,
    isConfirmedDelete: MutableState<Boolean>
) {

    Dialog(onDismissRequest = { confirmationDialog.value = false }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Text(
                    "Are you sure you want to delete this item ?",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.size(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {
                            confirmationDialog.value = false
                        },
                        modifier = Modifier
                            .width(75.dp)
                            .height(36.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                    ) {
                        Text(text = "Cancel", fontSize = 8.sp)
                    }
                    Button(
                        onClick = {
                            confirmationDialog.value = false
                            isConfirmedDelete.value = true
                        },
                        modifier = Modifier
                            .width(75.dp)
                            .height(36.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text(text = "Delete", fontSize = 8.sp)
                    }
                }
            }
        }
    }
}