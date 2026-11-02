package com.example.responses.rocketleague

import com.example.models.rocketleague.GameMode
import kotlinx.serialization.Serializable

@Serializable
data class CreateGameResultRequest(
    val mode: GameMode,
    val isWin: Boolean
)
