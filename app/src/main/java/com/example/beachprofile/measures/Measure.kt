package com.example.beachprofile.measures

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.beachprofile.sessions.Session
import java.time.LocalDateTime

@Entity(
    foreignKeys = [ForeignKey(
        entity = Session::class,
        parentColumns = ["id"],
        childColumns = ["sessionId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Measure(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sessionId: Int,
    val inclination: Float,
    val longitude: Double,
    val latitude: Double,
    val note: String,
    val timestamp: LocalDateTime
)