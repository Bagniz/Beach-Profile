package com.example.beachprofile.measures

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.beachprofile.database.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MeasureViewModel(context: Context, sessionId: Int) : ViewModel() {
    private val measureDao = AppDatabase.getDatabase(context).measureDao()
    var measures: Flow<List<Measure>> = measureDao.findBySessionId(sessionId)

    fun addMeasure(measure: Measure) {
        viewModelScope.launch {
            measureDao.insertMeasure(measure)
        }
    }

    fun deleteMeasureById(id: Int) {
        viewModelScope.launch {
            measureDao.deleteMeasureById(id)
        }
    }

    companion object {
        val CONTEXT_KEY = object : CreationExtras.Key<Context> {}
        val SESSION_KEY = object : CreationExtras.Key<Int> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context: Context = this[CONTEXT_KEY] as Context
                val sessionId: Int = this[SESSION_KEY] as Int
                MeasureViewModel(context, sessionId)
            }
        }
    }
}