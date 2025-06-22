package com.example.beachprofile.sessions

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
@Entity
data class Session(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val date: LocalDateTime
) : Parcelable