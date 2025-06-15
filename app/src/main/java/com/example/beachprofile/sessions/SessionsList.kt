package com.example.beachprofile.sessions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SessionsList(innerPaddingValues: PaddingValues) {
    var sessions = remember {
        mutableStateListOf<Session>()
    }
    var showAddSessionDialog = remember { mutableStateOf<Boolean>(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        if (sessions.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Button(onClick = { showAddSessionDialog.value = true }) {
                    Text("Create Session")
                }
            }
        } else {
            Scaffold(floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddSessionDialog.value = true },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Show add session form"
                    )
                }
            }, floatingActionButtonPosition = FabPosition.End, modifier = Modifier.padding(16.dp)) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    items(sessions) { session ->
                        SessionItem(session)
                    }
                }
            }
        }

        if (showAddSessionDialog.value) {
            AddSessionForm(showAddSessionDialog, sessions)
        }
    }
}