package com.example.models

import com.preciado.interfaces.data.IModel
import java.time.Instant

data class RocketLeagueGameResult(
    override val id: Long = 0,
    val mode: RocketLeagueGameMode = RocketLeagueGameMode.UNRANKED,
    val isWin: Boolean = false,
    val createdAt: Instant = Instant.now()
): IModel<Long>
