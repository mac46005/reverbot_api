package com.example.factories

import com.example.models.JoseVoiceLog
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object JoseVoiceLogFactory {
    fun build(
        id: Int = 0,
        timestamp: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    ): JoseVoiceLog {
        return JoseVoiceLog(
            id,
            timestamp
        )
    }


}