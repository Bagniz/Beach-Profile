package com.example.beachprofile.measures

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
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