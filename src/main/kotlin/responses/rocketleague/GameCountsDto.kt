package com.example.responses.rocketleague

import com.example.models.rocketleague.GameCounts
import kotlinx.serialization.Serializable

@Serializable
data class GameCountsDto(
    val wins: Long = 0,
    val losses: Long = 0
)

fun GameCounts.toDto(): GameCountsDto {
    return GameCountsDto (
        wins = this.wins,
        losses = this.losses
    )
}