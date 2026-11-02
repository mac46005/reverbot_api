package com.example.responses.rocketleague

import com.example.models.rocketleague.TodaysGameCounts
import kotlinx.serialization.Serializable

@Serializable
data class RocketLeagueTodaysGameCountsDto(
    val wins: Long = 0,
    val losses: Long = 0
)

fun TodaysGameCounts.toDto(): RocketLeagueTodaysGameCountsDto {
    return RocketLeagueTodaysGameCountsDto(
        wins = this.wins,
        losses = this.losses
    )
}