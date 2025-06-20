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

    fun addSession(session: Session) {
        viewModelScope.launch {
            sessionDao.insertSession(session)
        }
    }
}