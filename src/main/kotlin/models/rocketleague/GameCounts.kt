package com.example.models.rocketleague

import java.time.Instant

data class GameCounts(
    val date: Instant,
    val win: Long = 0,
    val losses: Long = 0
)
