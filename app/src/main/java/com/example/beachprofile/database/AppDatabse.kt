package com.example.beachprofile.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.beachprofile.measures.Measure
import com.example.beachprofile.measures.MeasureDao
import com.example.beachprofile.sessions.Session
import com.example.beachprofile.sessions.SessionDao

@Database(entities = [Measure::class, Session::class], version = 1)
abstract class AppDatabse: RoomDatabase() {
    abstract fun measureDao(): MeasureDao
    abstract fun sessionDao(): SessionDao
}