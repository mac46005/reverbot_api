package com.example.data.rocketleague

import com.example.models.rocketleague.GameMode
import com.example.models.rocketleague.GameResult
import com.preciado.data.column.ReadColumn
import com.preciado.data.utilities.getInstant
import com.preciado.jdbc.mapper.DataMapper
import java.sql.ResultSet

object GameResultDataMapper: DataMapper<GameResult>() {
    override val tableName: String
        get() = "rocket_league_tallies"
    override val tableAlias: String
        get() = "rlt"
    override val columnPrefix: String
        get() = "rocket_league_tallies"

    override fun getFromResultSet(
        prefix: String?,
        resultSet: ResultSet
    ): GameResult? {
        val id = resultSet.getLong("id")
        if (resultSet.wasNull()) return null

        return GameResult(
            id,
            GameMode.valueOf(resultSet.getString("mode")),
            resultSet.getBoolean("is_win"),
            resultSet.getInstant("created_at")
        )
    }

    override fun getCommonReadColumns(alias: String?): List<ReadColumn> {
        return emptyList()
    }
}