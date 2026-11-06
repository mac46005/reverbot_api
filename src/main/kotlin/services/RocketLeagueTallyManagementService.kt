package com.example.services

import com.example.data.rocketleague.RocketLeagueTalliesData
import com.example.models.rocketleague.GameCounts
import com.example.models.rocketleague.GameMode
import com.example.models.rocketleague.GameResult
import com.example.models.rocketleague.MvpInfo
import java.time.Clock
import java.time.Instant

class RocketLeagueTallyManagementService(
    private val rocketLeagueTalliesData: RocketLeagueTalliesData,
    private val clock: Clock
) {
    suspend fun createGameResult(mode: GameMode, isWin: Boolean, mvp: String): Long {
        return rocketLeagueTalliesData.createGameResult(
            GameResult(
                mode = mode,
                isWin = isWin,
                mvp = mvp
            )
        )
    }




    suspend fun getUnrankedGameResultsFromToday(): List<GameResult> {
        val today = Instant.now(clock)
        return rocketLeagueTalliesData.getUnrankedGameResultsFromToday(today)
    }

    suspend fun getUnrankedGameCountsForToday(): GameCounts {
        val today = Instant.now(clock)

        return rocketLeagueTalliesData.getUnrankedGameCountsForToday(today)
    }






    suspend fun getUnrankedGameResultsForPastMonth(): List<GameResult> {
        val today = Instant.now(clock)
        return rocketLeagueTalliesData.getUnrankedGameResultsForPastMonth(today)
    }

    suspend fun getUnrankedGameCountsForPastMonth(): GameCounts {
        val today = Instant.now(clock)
        return rocketLeagueTalliesData.getUnrankedGameCountsForPastMonth(today)
    }

    suspend fun updateGameResult(id: Long, mode: GameMode, isWin: Boolean): Boolean {
        require(id > 0) {
            "Please provide an existing game id"
        }

        return rocketLeagueTalliesData.updateGameResult(
            GameResult(
                id = id,
                mode = mode,
                isWin = isWin
            )
        )
    }

    suspend fun deleteGameResult(id: Long): Boolean {
        require(id > 0) {
            "Please provide an existing game id"
        }

        return rocketLeagueTalliesData.deleteGameResult(id)
    }

    suspend fun getUnrankedMvpRationsForToday(): List<MvpInfo> {
        val today = Instant.now(clock)
        return rocketLeagueTalliesData.getUnrankedMvpRatiosForToday(today)
    }
}