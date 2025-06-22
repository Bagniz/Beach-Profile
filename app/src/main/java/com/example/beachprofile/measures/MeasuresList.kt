package com.example.beachprofile.measures

import android.util.Log
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

@Composable
fun MeasuresList(
    sessionId: Int,
    innerPaddingValues: PaddingValues,
    inclination: MutableFloatState,
    latitude: MutableDoubleState,
    longitude: MutableDoubleState,
    startRegistering: () -> Unit,
    stopRegistering: () -> Unit
) {
    val extras = MutableCreationExtras()
    extras[MeasureViewModel.CONTEXT_KEY] = LocalContext.current
    extras[MeasureViewModel.SESSION_KEY] = sessionId

    val measuresModel: MeasureViewModel = viewModel(factory = MeasureViewModel.Factory, extras = extras)
    val measures by measuresModel.measures.collectAsState(initial = emptyList())
    var showAddMeasureDialog = remember { mutableStateOf(false) }

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
            Scaffold(floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        showAddMeasureDialog.value = true
                    },
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
                    items(measures) { measure ->
                        MeasureItem(measure, measuresModel)
                    }
                }
            }
        }

        if (showAddMeasureDialog.value) {
            AddMeasureForm(
                sessionId,
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