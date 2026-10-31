package com.example.services

import com.example.data.RocketLeagueTalliesData
import com.example.models.RocketLeagueGameMode
import com.example.models.RocketLeagueGameResult
import com.example.models.RocketLeagueTodaysGameCounts
import java.time.Clock
import java.time.Instant

class RocketLeagueTallyManagementService(
    private val rocketLeagueTalliesData: RocketLeagueTalliesData,
    private val clock: Clock
) {
    suspend fun createGameResult(mode: RocketLeagueGameMode, isWin: Boolean): Long {
        return rocketLeagueTalliesData.createGameResult(
            RocketLeagueGameResult(
                mode = mode,
                isWin = isWin
            )
        )
    }


    suspend fun getGameResultsFromToday(): List<RocketLeagueGameResult> {
        val today = Instant.now(clock)
        return rocketLeagueTalliesData.getGameResultsFromToday(
            today
        )
    }

    suspend fun getGameCountsForToday(): RocketLeagueTodaysGameCounts {
        val today = Instant.now(clock)
        return rocketLeagueTalliesData.getGameCountsForToday(
            today
        )
    }

    suspend fun updateGameResult(id: Long, mode: RocketLeagueGameMode, isWin: Boolean): Boolean {
        require(id > 0) {
            "Please provide an existing game id"
        }

        return rocketLeagueTalliesData.updateGameResult(
            RocketLeagueGameResult(
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
}