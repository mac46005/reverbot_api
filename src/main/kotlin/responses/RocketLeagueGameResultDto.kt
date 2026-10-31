package com.example.responses

import com.example.models.RocketLeagueGameMode
import com.example.models.RocketLeagueGameResult
import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import kotlinx.serialization.Serializable

@Serializable
data class RocketLeagueGameResultDto(
    val id: Long,
    val mode: RocketLeagueGameMode,
    val isWin: Boolean,
    val createdAt: Instant
)



fun RocketLeagueGameResult.toDto(): RocketLeagueGameResultDto {
    return RocketLeagueGameResultDto(
        id = this.id,
        mode = this.mode,
        isWin = this.isWin,
        createdAt = this.createdAt.toKotlinInstant()
    )
}

fun List<RocketLeagueGameResult>.toDtoList(): List<RocketLeagueGameResultDto> {
    return map {
        it.toDto()
    }
}