package com.example.beachprofile.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.beachprofile.measures.Measure
import com.example.beachprofile.measures.MeasureDao
import com.example.beachprofile.sessions.Session
import com.example.beachprofile.sessions.SessionDao

@Database(entities = [Measure::class, Session::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun measureDao(): MeasureDao
    abstract fun sessionDao(): SessionDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "note_database"
                ).build().also { INSTANCE = it }
            }
    }
}