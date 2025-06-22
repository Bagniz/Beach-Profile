package com.example.beachprofile.sessions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Query("SELECT * FROM session")
    fun findAll(): Flow<List<Session>>

    @Transaction
    @Query("SELECT * FROM session")
    fun findAllWithMeasures(): List<SessionWithMeasures>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: Session)

    @Transaction
    @Query("DELETE FROM session WHERE id = :id")
    suspend fun deleteSessionById(id: Int)
}