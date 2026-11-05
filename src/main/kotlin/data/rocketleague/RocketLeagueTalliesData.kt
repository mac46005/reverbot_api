package com.example.data.rocketleague

import com.example.models.rocketleague.GameCounts
import com.example.models.rocketleague.GameMode
import com.example.models.rocketleague.GameResult
import com.example.models.rocketleague.TodaysGameCounts
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

    private fun getBeginningOfDayAndBeginningOfNextDay(today: Instant): Pair<Instant, Instant> {
        val zone = ZoneId.of("America/Chicago")

//        val yearMonth = YearMonth.from(
//            today.atZone(zone)
//        )
//
//        val startOfMonth = yearMonth
//            .atDay(1)
//            .atStartOfDay(zone)
//            .toInstant()
//
//        val startOfNextMonth = yearMonth
//            .plusMonths(1)
//            .atDay(1)
//            .atStartOfDay(zone)
//            .toInstant()
//
//

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
                )
            ),
            setValues = { statement ->
                var colCount = 1
                statement.setString(colCount++,result.mode.mode)
                statement.setBoolean(colCount++, result.isWin)
            },
            readId = { resultSet ->
                getIdLong(resultSet)
            }
        )
    }




    suspend fun getGameResultsFromToday(today: Instant): List<GameResult> = withContext(Dispatchers.IO) {
        jdbcCrudOperations.dataSource.connection.use { connection ->
            val selectStatement = """
                SELECT 
                    id, 
                    mode, 
                    is_win, 
                    created_at
                FROM ${jdbcCrudOperations.tableName}
                    WHERE created_at >= ?
                    AND created_at < ?
                    ORDER BY created_at DESC, id DESC;
            """.trimIndent()

            connection.prepareStatement(selectStatement).use { statement ->
                var colCount = 1

                val thisDayRange = getBeginningOfDayAndBeginningOfNextDay(today)

                statement.setInstant(colCount++, thisDayRange.first)
                statement.setInstant(colCount++, thisDayRange.second)

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

    suspend fun getGameCountsForToday(today: Instant): TodaysGameCounts = withContext(Dispatchers.IO) {
        jdbcCrudOperations.dataSource.connection.use { connection ->
            val selectStatement = """
                SELECT
                    COUNT(*) FILTER (WHERE is_win = TRUE) AS wins,
                    COUNT(*) FILTER (WHERE is_win = FALSE) AS losses
                FROM ${jdbcCrudOperations.tableName}
                WHERE created_at >= ?
                    AND created_at < ?;
            """.trimIndent()

            connection.prepareStatement(selectStatement).use { statement ->
                var colCount = 1

                val (startOfDay, startOfNextDay) = getBeginningOfDayAndBeginningOfNextDay(today)

                statement.setInstant(colCount++, startOfDay)
                statement.setInstant(colCount++, startOfNextDay)

                statement.executeQuery().use { resultSet ->
                    if (resultSet.next()) {
                        TodaysGameCounts(
                            resultSet.getLong("wins"),
                            resultSet.getLong("losses")
                        )
                    } else {
                        TodaysGameCounts()
                    }
                }
            }
        }
    }



















    suspend fun getUnrankedGameResultsFromToday(today: Instant): List<GameResult> =
        withContext(Dispatchers.IO) {
            jdbcCrudOperations.dataSource.connection.use { connection ->
                val selectStatement = """
                SELECT
                    id,
                    mode,
                    is_win,
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

    // TODO Add MVP
    suspend fun getUnrankedGameCountsForToday(today: Instant): TodaysGameCounts =
        withContext(Dispatchers.IO) {
            jdbcCrudOperations.dataSource.connection.use { connection ->
                val selectStatement = """
                SELECT
                    COUNT(*) FILTER (WHERE is_win = TRUE AND mode = 'UNRANKED') AS wins,
                    COUNT(*) FILTER (WHERE is_win = FALSE AND mode = 'UNRANKED') AS losses
                FROM ${jdbcCrudOperations.tableName}
                WHERE created_at >= ?
                    AND created_at < ?;
            """.trimIndent()


                connection.prepareStatement(selectStatement).use { statement ->
                    var colCount = 1
                    val (startOfDay, startOfNextDay) = getBeginningOfDayAndBeginningOfNextDay(today)

                    statement.setInstant(colCount++, startOfDay)
                    statement.setInstant(colCount++, startOfNextDay)

                    statement.executeQuery().use { resultSet ->
                        if (resultSet.next()) {
                            TodaysGameCounts(
                                resultSet.getLong("wins"),
                                resultSet.getLong("losses")
                            )
                        } else {
                            TodaysGameCounts()
                        }
                    }
                }
            }
        }














    // TODO Complete /past-three-months-unranked
//    suspend fun getUnrankedGameResultsForMonth(month: Instant): List<GameCounts> {
//        jdbcCrudOperations.dataSource.connection.use { connection ->
//            val selectStatement = """
//                SELECT
//                    COUNT(*) FILTER (WHERE is_win = TRUE AND mode = 'UNRANKED'),
//                    COUNT(*) FILTER (WHERE is_win = FALSE AND mode = 'UNRANKED')
//                FROM ${jdbcCrudOperations.tableName}
//                WHERE created_at >= ?
//            """.trimIndent()
//
//
//            connection.prepareStatement(selectStatement).use { statement ->
//                var colCount = 1
//            }
//        }
//    }













    suspend fun updateGameResult(result: GameResult): Boolean {
        return jdbcCrudOperations.update(
            writeColumns = listOf(
                WriteColumn(
                    "mode"
                ),
                WriteColumn(
                    "is_win"
                )
            ),
            predicates = listOf(
                "id"
            ),
            setValues = { statement ->
                var colCount = 1
                statement.setString(colCount++, result.mode.mode)
                statement.setBoolean(colCount++, result.isWin)
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