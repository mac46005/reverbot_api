package com.example.models

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class JosePlaySoonLog(
    val id: Int = 0,
    val timestamp: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
)
