package com.example.responses

import com.example.models.RocketLeagueGameMode
import kotlinx.serialization.Serializable

@Serializable
data class CreateRocketLeagueGameResultRequest(
    val mode: RocketLeagueGameMode,
    val isWin: Boolean
)
