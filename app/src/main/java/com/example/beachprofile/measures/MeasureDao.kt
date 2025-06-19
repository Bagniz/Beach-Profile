package com.example.beachprofile.measures

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MeasureDao {
    @Query("SELECT * FROM measure WHERE id == :id")
    fun findById(id: Int): Measure

    @Query("SELECT * FROM measure WHERE sessionId = :sessionId")
    fun findBySessionId(sessionId: Int): List<Measure>

    @Insert
    fun insertMeasure(measure: Measure)

    @Delete
    fun deleteMeasureById(id: Int)
}