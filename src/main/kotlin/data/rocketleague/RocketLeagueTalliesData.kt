package com.example.data.rocketleague

import com.example.models.rocketleague.GameCounts
import com.example.models.rocketleague.GameMode
import com.example.models.rocketleague.GameResult
import com.preciado.data.column.WriteColumn
import com.preciado.data.sqlmodifier.getIdLong
import com.preciado.data.utilities.setInstant
import com.preciado.exceptions.NotFoundException
import com.preciado.jdbc.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import javax.sql.DataSource

class RocketLeagueTalliesData(
    dataSource: DataSource
): Data<Long, GameResult>(
    "rocket_league_tallies",
    "rlt",
    dataSource
) {
    private val zone = ZoneId.of("America/Chicago")
    private fun getPastMonth(today: Instant): Instant {
        val date = today.atZone(zone).toLocalDate()
        val pastMonth = date
            .minusMonths(1)
            .atStartOfDay(zone)
            .toInstant()
        
        return pastMonth
    }

    private fun getPastXMonthUpToToday(pastNumberOfMonths: Long, today: Instant): Instant{
        require(pastNumberOfMonths > 0) {
            "Please input a positive number"
        }

        val date = today.atZone(zone).toLocalDate()
        val pastXMonths = date
            .minusMonths(pastNumberOfMonths)
            .atStartOfDay(zone)
            .toInstant()
        
        return pastXMonths
    }

    private fun getBeginningOfDayAndBeginningOfNextDay(today: Instant): Pair<Instant, Instant> {

        val date = today.atZone(zone).toLocalDate()

        val startOfDay = date
            .atStartOfDay(zone)
            .toInstant()

        val startOfNextDay = date
            .plusDays(1)
            .atStartOfDay(zone)
            .toInstant()



        return Pair(startOfDay, startOfNextDay)
    }

    suspend fun createGameResult(result: GameResult): Long {
        return jdbcCrudOperations.create(
            writeColumns = listOf(
                WriteColumn(
                    "mode"
                ),
                WriteColumn(
                    "is_win"
                ),
		        WriteColumn(
			        "mvp"
		        )
            ),
            setValues = { statement ->
                var colCount = 1
                statement.setString(colCount++,result.mode.mode)
                statement.setBoolean(colCount++, result.isWin)
		        statement.setString(colCount++, result.mvp)
            },
            readId = { resultSet ->
                getIdLong(resultSet)
            }
        )
    }
















































































    suspend fun getUnrankedGameResultsFromToday(today: Instant): List<GameResult> =
        withContext(Dispatchers.IO) {
            jdbcCrudOperations.dataSource.connection.use { connection ->
                val selectStatement = """
                SELECT
                    id,
                    mode,
                    is_win,
                    mvp,
                    created_at
                FROM ${jdbcCrudOperations.tableName}
                    WHERE mode = ?
                    AND created_at >= ?
                    AND created_at < ?
                    ORDER BY created_at DESC, id DESC;
            """.trimIndent()

                connection.prepareStatement(selectStatement).use { statement ->
                    var colCount = 1

                    val (startOfDay, startOfNextDay) = getBeginningOfDayAndBeginningOfNextDay(today)
                    statement.setString(colCount++, GameMode.UNRANKED.mode)
                    statement.setInstant(colCount++, startOfDay)
                    statement.setInstant(colCount++, startOfNextDay)

                    statement.executeQuery().use { resultSet ->
                        val list = mutableListOf<GameResult>()

                        while (resultSet.next()) {
                            val result = GameResultDataMapper.getFromResultSet(resultSet = resultSet)
                            if (result == null) {
                                throw NotFoundException("Unable to find Rocket League Game Result")
                            }
                            list.add(
                                result
                            )
                        }

                        return@withContext list
                    }
                }
            }
        }

        // change outut to GameCounts
    suspend fun getUnrankedGameCountsForToday(today: Instant): GameCounts =
        withContext(Dispatchers.IO) {
            jdbcCrudOperations.dataSource.connection.use { connection ->
                val selectStatement = """
                SELECT
                    COUNT(*) FILTER (WHERE is_win = TRUE) AS wins,
                    COUNT(*) FILTER (WHERE is_win = FALSE) AS losses
                FROM ${jdbcCrudOperations.tableName}
                WHERE mode = 'UNRANKED'
                    AND created_at >= ?
                    AND created_at < ?;
            """.trimIndent()


                connection.prepareStatement(selectStatement).use { statement ->
                    var colCount = 1
                    val (startOfDay, startOfNextDay) = getBeginningOfDayAndBeginningOfNextDay(today)

                    statement.setInstant(colCount++, startOfDay)
                    statement.setInstant(colCount++, startOfNextDay)

                    statement.executeQuery().use { resultSet ->
                        if (resultSet.next()) {
                            GameCounts(
                                wins = resultSet.getLong("wins"),
                                losses = resultSet.getLong("losses")
                            )
                        } else {
                            GameCounts()
                        }
                    }
                }
            }
        }












suspend fun getUnrankedGameResultsForPastMonth(today: Instant): List<GameResult> = withContext(Dispatchers.IO) {
    jdbcCrudOperations.dataSource.connection.use { connection -> 
        val selectStatement = """
            SELECT
                id,
                mode,
                is_win,
                mvp,
                created_at
            FROM ${jdbcCrudOperations.tableName}
            WHERE mode = 'UNRANKED'
                AND created_at >= ?
                AND created_at < ?;
        """

        connection.prepareStatement(selectStatement).use { statement ->
            var colCount = 1
            val pastMonth = getPastMonth(today)
            statement.setInstant(colCount++, pastMonth)
            statement.setInstant(colCount++, today)

            statement.executeQuery().use { resultSet ->
                val list = mutableListOf<GameResult>()
                
                while (resultSet.next()) {
                    val gameResult = GameResultDataMapper.getFromResultSet(resultSet = resultSet)
                        ?: throw NotFoundException("Unable to retrieve GameResult from getUnrankedGameResultsForPastMonth in rocket_league_tallies database")

                    list.add(
                        gameResult
                    )
                }

                return@withContext list
            }
        }
    }
}


suspend fun getUnrankedGameCountsForPastMonth(today: Instant): GameCounts = withContext(Dispatchers.IO) {
    jdbcCrudOperations.dataSource.connection.use { connection -> 
        val selectStatement = """
            SELECT
                COUNT(*) FILTER (WHERE is_win = TRUE) AS wins,
                COUNT(*) FILTER (WHERE is_win = FALSE) AS losses
            FROM ${jdbcCrudOperations.tableName}
            WHERE mode = 'UNRANKED'
                AND created_at >= ?
                AND created_at < ?
        """

        connection.prepareStatement(selectStatement).use { statement ->
            var colCount = 1
            val pastMonth = getPastMonth(today)
            statement.setInstant(colCount++, pastMonth)
            statement.setInstant(colCount++, today)

            statement.executeQuery().use { resultSet ->
                if (resultSet.next()) {
                    GameCounts(
                        wins = resultSet.getLong("wins"),
                        losses = resultSet.getLong("losses")
                    )
                } else {
                    GameCounts()
                }
            }
        }
    }
}









    suspend fun updateGameResult(result: GameResult): Boolean {
        return jdbcCrudOperations.update(
            writeColumns = listOf(
                WriteColumn(
                    "mode"
                ),
                WriteColumn(
                    "is_win"
                ),
                WriteColumn(
                    "mvp"
                )
            ),
            predicates = listOf(
                "id"
            ),
            setValues = { statement ->
                var colCount = 1
                statement.setString(colCount++, result.mode.mode)
                statement.setBoolean(colCount++, result.isWin)
                statement.setString(colCount++, result.mvp)
                statement.setLong(colCount++, result.id)
            }
        )
    }

    suspend fun deleteGameResult(id: Long): Boolean {
        return jdbcCrudOperations.delete(
            predicates = listOf(
                "id"
            ),
            setValues = { statement ->
                var colCount = 1
                statement.setLong(colCount, id)
            }
        )
    }
}
