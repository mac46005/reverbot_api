package com.example.data

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.TimeZone

@Serializable
data class CreateJosePlaySoonRequest(
    val timestamp: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
)




@Serializable
data class CreateJoseVoiceRequest(val timestamp: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))