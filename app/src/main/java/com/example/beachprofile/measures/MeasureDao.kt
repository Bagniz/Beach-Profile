package com.example.beachprofile.measures

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasureDao {
    @Query("SELECT * FROM measure WHERE sessionId = :sessionId")
    fun findBySessionId(sessionId: Int): Flow<List<Measure>>

    @Transaction
    @Insert
    suspend fun insertMeasure(measure: Measure)

    @Transaction
    @Query("DELETE FROM measure WHERE id = :id")
    suspend fun deleteMeasureById(id: Int)
}