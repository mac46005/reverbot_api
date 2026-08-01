package com.example.factories

import com.example.models.JosePlaySoonLog
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object JosePlaySoonLogFactory {
    fun build(
        id: Int = 0,
        timestamp: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    ): JosePlaySoonLog {
        return JosePlaySoonLog(
            id,
            timestamp
        )
    }
}