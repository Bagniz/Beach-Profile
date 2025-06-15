package com.example.beachprofile.measures

import java.time.LocalDateTime

data class Measure(
    var inclination: Float,
    var longitude: Double,
    var latitude: Double,
    val note: String,
    val timestamp: LocalDateTime
)