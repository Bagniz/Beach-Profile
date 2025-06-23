package com.example.beachprofile.measures

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.SsidChart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beachprofile.common.InclinationGraph
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
    var showInclinationChart = remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
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
            Scaffold(bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    IconButton(
                        onClick = {
                            saveCsvDocumentLauncher.launch("${session.name}.csv")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FileDownload,
                            contentDescription = "Edit"
                        )
                    }

                    IconButton(
                        onClick = { showAddMeasureDialog.value = true },
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    }

                    IconButton(
                        onClick = {
                            showInclinationChart.value = true
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.SsidChart,
                            contentDescription = "Chart"
                        )
                    }
                }
            }
            ) { it ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    state = listState
                ) {
                    items(measures) { measure ->
                        MeasureItem(measure, measuresModel)
                    }
                }
            }
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
                stopRegistering,
            ) {
                if (measures.size > 5) {
                    coroutineScope.launch {
                        listState.animateScrollToItem(measures.size)
                    }
                }
            }
        }

        if (showInclinationChart.value) {
            InclinationGraph(showInclinationChart, measures)
        }
    }
}