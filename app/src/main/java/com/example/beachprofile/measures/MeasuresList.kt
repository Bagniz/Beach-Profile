package com.example.beachprofile.measures

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
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beachprofile.sessions.Session
import com.example.beachprofile.sessions.convertMeasuresToCSV
import com.example.beachprofile.sessions.saveCSVToUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MeasuresList(
    session: Session,
    innerPaddingValues: PaddingValues,
    inclination: MutableFloatState,
    latitude: MutableDoubleState,
    longitude: MutableDoubleState,
    startRegistering: () -> Unit,
    stopRegistering: () -> Unit
) {
    val extras = MutableCreationExtras()
    extras[MeasureViewModel.CONTEXT_KEY] = LocalContext.current
    extras[MeasureViewModel.SESSION_KEY] = session.id

    val measuresModel: MeasureViewModel =
        viewModel(factory = MeasureViewModel.Factory, extras = extras)
    val measures by measuresModel.measures.collectAsState(initial = emptyList())
    var showAddMeasureDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val saveCsvDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv"),
        onResult = { uri: Uri? ->
            uri?.let {
                CoroutineScope(Dispatchers.IO).launch {
                    Log.i(uri.toString(), "LOG")
                    val csv =
                        convertMeasuresToCSV(measures, session)
                    saveCSVToUri(context, uri, csv)
                }
            }
        }
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues),
    ) {
        if (measures.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Button(onClick = { showAddMeasureDialog.value = true }) {
                    Text("Create measurement")
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
                            items(measures) { measure ->
                                MeasureItem(measure, measuresModel)
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
                                onClick = { showAddMeasureDialog.value = true },
                                shape = CircleShape,
                                containerColor = MaterialTheme.colorScheme.primary
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                            }

                            FloatingActionButton(
                                onClick = {
                                    saveCsvDocumentLauncher.launch("${session.name}.csv")
                                },
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

        if (showAddMeasureDialog.value) {
            AddMeasureForm(
                session.id,
                measuresModel,
                showAddMeasureDialog,
                inclination,
                latitude,
                longitude,
                startRegistering,
                stopRegistering
            )
        }
    }
}