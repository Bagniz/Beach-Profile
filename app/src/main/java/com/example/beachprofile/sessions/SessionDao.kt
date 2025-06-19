package com.example.beachprofile.sessions

import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Query

@Entity
interface SessionDao {
    @Query("SELECT * FROM session")
    fun findAll(): List<Session>

    @Query("SELECT * FROM session WHERE id = :id")
    fun findById(id: Int): Session

    @Insert
    fun insertSession(session: Session)

    @Delete
    fun deleteSession(id: Int)
}