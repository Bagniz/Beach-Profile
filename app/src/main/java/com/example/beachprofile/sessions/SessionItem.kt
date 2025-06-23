package com.example.beachprofile.sessions

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.beachprofile.common.ConfirmationDialogue
import com.example.beachprofile.measures.MeasuresActivity

@Composable
fun SessionItem(session: Session, sessionsModel: SessionViewModel) {
    val context = LocalContext.current
    var showConfirmationDialog = remember { mutableStateOf(false) }
    var isConfirmedDelete = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
    ) {
        Card(
            onClick = {
                val intent = Intent(context, MeasuresActivity::class.java)
                intent.putExtra("sessionId", session.id)
                intent.putExtra("sessionName", session.name)
                intent.putExtra("sessionDate", session.date.toString())
                context.startActivity(intent)
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { showConfirmationDialog.value = true },
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
                Text(
                    text = session.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(200.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = session.date.format(FORMATTER),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    if (showConfirmationDialog.value) {
        ConfirmationDialogue(showConfirmationDialog, isConfirmedDelete)
    }

    if (isConfirmedDelete.value) {
        sessionsModel.deleteSessionById(session.id)
        isConfirmedDelete.value = false
    }
}