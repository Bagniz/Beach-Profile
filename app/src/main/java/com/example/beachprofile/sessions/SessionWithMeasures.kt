package com.example.beachprofile.sessions

import androidx.room.Embedded
import androidx.room.Relation
import com.example.beachprofile.measures.Measure

data class SessionWithMeasures(
    @Embedded val session: Session,
    @Relation(
        parentColumn = "id",
        entityColumn = "sessionId"
    )
    val measures: List<Measure>
)