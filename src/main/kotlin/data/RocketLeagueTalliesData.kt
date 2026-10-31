package com.example.data

import com.example.models.RocketLeagueGameMode
import com.example.models.RocketLeagueGameResult
import com.example.models.RocketLeagueTodaysGameCounts
import com.preciado.data.column.WriteColumn
import com.preciado.data.sqlmodifier.getIdLong
import com.preciado.data.utilities.getInstant
import com.preciado.data.utilities.setInstant
import com.preciado.jdbc.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import javax.sql.DataSource

class RocketLeagueTalliesData(
    dataSource: DataSource
): Data<Long, RocketLeagueGameResult>(
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

    suspend fun createGameResult(result: RocketLeagueGameResult): Long {
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


    suspend fun getGameResultsFromToday(today: Instant): List<RocketLeagueGameResult> = withContext(Dispatchers.IO) {
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
                    val list = mutableListOf<RocketLeagueGameResult>()

                    while (resultSet.next()) {
                        list.add(
                            RocketLeagueGameResult(
                                resultSet.getLong("id"),
                                RocketLeagueGameMode.valueOf(resultSet.getString("mode")),
                                resultSet.getBoolean("is_win"),
                                resultSet.getInstant("created_at")
                            )
                        )
                    }

                    return@withContext list
                }
            }
        }
    }




    suspend fun getWinningCountFromToday(today: Instant): Long = withContext(Dispatchers.IO) {
        jdbcCrudOperations.dataSource.connection.use { connection ->
            val countStatement = """
                SELECT COUNT(*) AS count
                FROM ${jdbcCrudOperations.tableName}
                WHERE is_win = ?
                    AND created_at >= ?
                    AND created_at < ?;
            """.trimIndent()

            connection.prepareStatement(countStatement).use { statement ->
                var colCount = 1

                val thisDayRange = getBeginningOfDayAndBeginningOfNextDay(today)



                statement.setBoolean(colCount++, true)
                statement.setInstant(colCount++, thisDayRange.first)
                statement.setInstant(colCount++, thisDayRange.second)

                statement.executeQuery().use{ resultSet ->
                    if (resultSet.next()) {
                        resultSet.getLong("count")
                    } else {
                        0
                    }
                }
            }
        }
    }



    suspend fun getLossesCountFromToday(today: Instant): Long = withContext(Dispatchers.IO) {
        jdbcCrudOperations.dataSource.connection.use { connection ->
            val countStatement = """
                SELECT COUNT(*) AS count
                FROM ${jdbcCrudOperations.tableName}
                WHERE is_win = ?
                    AND created_at >= ?
                    AND created_at < ?
            """.trimIndent()

            connection.prepareStatement(countStatement).use { statement ->
                var colCount = 1

                val thisDayRange = getBeginningOfDayAndBeginningOfNextDay(today)



                statement.setBoolean(colCount++, false)
                statement.setInstant(colCount++, thisDayRange.first)
                statement.setInstant(colCount++, thisDayRange.second)

                statement.executeQuery().use{ resultSet ->
                    if (resultSet.next()) {
                        resultSet.getLong("count")
                    } else {
                        0
                    }
                }
            }
        }
    }




    suspend fun getGameCountsForToday(today: Instant): RocketLeagueTodaysGameCounts = withContext(Dispatchers.IO) {
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
                        RocketLeagueTodaysGameCounts(
                            resultSet.getLong("wins"),
                            resultSet.getLong("losses")
                        )
                    } else {
                        RocketLeagueTodaysGameCounts()
                    }
                }
            }
        }
    }






    suspend fun updateGameResult(result: RocketLeagueGameResult): Boolean {
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