package com.example.beachprofile.sessions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SessionsList(innerPaddingValues: PaddingValues) {
    var showAddSessionDialog = remember { mutableStateOf<Boolean>(false) }
    val sessionsModel: SessionViewModel = viewModel()
    val sessions by sessionsModel.sessions.collectAsState(initial = emptyList())
    val context = LocalContext.current

    val folderPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                CoroutineScope(Dispatchers.IO).launch {
                    Log.i(uri.toString(), "LOG")
                    val sessionsWithMeasures = sessionsModel.getSessionsWithMeasures()
                    batchExportCSVsToFolder(context, uri, sessionsWithMeasures)
                }
            }
        }
    )

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
            Scaffold(
                content = { padding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            items(sessions) { session ->
                                SessionItem(session, sessionsModel)
                            }
                        }

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            FloatingActionButton(
                                onClick = { showAddSessionDialog.value = true },
                                shape = CircleShape,
                                containerColor = MaterialTheme.colorScheme.primary
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                            }

                            FloatingActionButton(
                                onClick = { folderPickerLauncher.launch(null) },
                                containerColor = MaterialTheme.colorScheme.secondary
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FileDownload,
                                    contentDescription = "Edit"
                                )
                            }
                        }
                    }
                }
            )
        }

        if (showAddSessionDialog.value) {
            AddSessionForm(showAddSessionDialog, sessionsModel)
        }
    }
}
