package com.example.responses.rocketleague

import com.example.models.rocketleague.GameMode
import com.example.models.rocketleague.GameResult
import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import kotlinx.serialization.Serializable

@Serializable
data class RocketLeagueGameResultDto(
    val id: Long,
    val mode: GameMode,
    val isWin: Boolean,
    val createdAt: Instant
)



fun GameResult.toDto(): RocketLeagueGameResultDto {
    return RocketLeagueGameResultDto(
        id = this.id,
        mode = this.mode,
        isWin = this.isWin,
        createdAt = this.createdAt.toKotlinInstant()
    )
}

fun List<GameResult>.toDtoList(): List<RocketLeagueGameResultDto> {
    return map {
        it.toDto()
    }
}