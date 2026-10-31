package com.example.responses

import com.example.models.RocketLeagueTodaysGameCounts

data class RocketLeagueTodaysGameCountsDto(
    val wins: Long = 0,
    val losses: Long = 0
)

fun RocketLeagueTodaysGameCounts.toDto(): RocketLeagueTodaysGameCountsDto {
    return RocketLeagueTodaysGameCountsDto(
        wins = this.wins,
        losses = this.losses
    )
}