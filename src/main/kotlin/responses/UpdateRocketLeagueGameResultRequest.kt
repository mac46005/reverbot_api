package com.example.responses

import com.example.models.RocketLeagueGameMode
import kotlinx.serialization.Serializable

@Serializable
data class UpdateRocketLeagueGameResultRequest(
    val mode: RocketLeagueGameMode,
    val isWin: Boolean
)
