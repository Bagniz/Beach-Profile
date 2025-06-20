package com.example.beachprofile.measures

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasureDao {
    @Query("SELECT * FROM measure WHERE sessionId = :sessionId")
    fun findBySessionId(sessionId: Int): Flow<List<Measure>>

    @Insert
    suspend fun insertMeasure(measure: Measure)

    @Delete
    suspend fun deleteMeasureById(measure: Measure)
}