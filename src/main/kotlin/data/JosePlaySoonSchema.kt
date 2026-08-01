package com.example.data

import com.example.exceptions.SqlINSERTException
import com.example.factories.JosePlaySoonLogFactory
import com.example.models.JosePlaySoonLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.sql.Connection
import java.sql.Statement
import java.sql.Timestamp

class JosePlaySoonSchema(
    private val connection: Connection,
) {
    companion object {
        private const val TABLE_NAME = "jose_play_soon"
        private const val INSERT = "INSERT INTO $TABLE_NAME (timestamp) VALUES (?)"
        private const val SELECT_ALL = "SELECT * FROM $TABLE_NAME"
    }




    suspend fun insert(josePlaySoonRequest: CreateJosePlaySoonRequest): Int = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)

        statement.setTimestamp(1, Timestamp.valueOf(josePlaySoonRequest.timestamp.toJavaLocalDateTime()))

        statement.execute()

        val generatedKeys = statement.generatedKeys

        if (generatedKeys.next()) {
            return@withContext generatedKeys.getInt(0)
        } else {
            throw SqlINSERTException(from = "JosePlaySoonSchema.insert")
        }
    }



    suspend fun selectAll(): List<JosePlaySoonLog> = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_ALL)
        val resultSet = statement.executeQuery()

        val list = mutableListOf<JosePlaySoonLog>()

        while (resultSet.next()) {
            val id = resultSet.getInt(1)
            val timestamp = resultSet.getTimestamp(2).toLocalDateTime().toKotlinLocalDateTime()

            val newJosePlaySoonLog = JosePlaySoonLogFactory.build(id, timestamp)
            list.add(newJosePlaySoonLog)
        }

        return@withContext list
    }
}