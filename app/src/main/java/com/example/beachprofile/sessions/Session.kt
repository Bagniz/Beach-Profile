package com.example.beachprofile.sessions

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Session(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val date: LocalDateTime
)