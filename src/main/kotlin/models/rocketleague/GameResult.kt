package com.example.models.rocketleague

import com.preciado.interfaces.data.IModel
import java.time.Instant

data class GameResult(
    override val id: Long = 0,
    val mode: GameMode = GameMode.UNRANKED,
    val isWin: Boolean = false,
    val createdAt: Instant = Instant.now()
): IModel<Long>
