package com.example.beachprofile.sessions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.beachprofile.database.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SessionViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionDao = AppDatabase.getDatabase(application).sessionDao()
    val sessions: Flow<List<Session>> = sessionDao.findAll()

    fun getSessionsWithMeasures(): List<SessionWithMeasures> {
        return sessionDao.findAllWithMeasures()
    }

    fun addSession(session: Session) {
        viewModelScope.launch {
            sessionDao.insertSession(session)
        }
    }

    fun deleteSessionById(id: Int) {
        viewModelScope.launch {
            sessionDao.deleteSessionById(id)
        }
    }
}