package com.example.data

import com.example.exceptions.SqlINSERTException
import com.example.factories.JoseVoiceLogFactory
import com.example.models.JoseVoiceLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.sql.Connection
import java.sql.Statement
import java.sql.Timestamp

class JoseVoiceSchema (
    private val connection: Connection
) {
    companion object {
        private const val TABLE_NAME = "jose_voice"
        private const val INSERT = "INSERT INTO $TABLE_NAME (timestamp) VALUES (?)"
        private const val SELECT_ALL = "SELECT * FROM $TABLE_NAME"
    }


    suspend fun insert(joseVoiceRequest: CreateJoseVoiceRequest): Int = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)
        statement.setTimestamp(1, Timestamp.valueOf(joseVoiceRequest.timestamp.toJavaLocalDateTime()))
        statement.execute()
        val generatedKeys = statement.generatedKeys

        if (generatedKeys.next()) {
            return@withContext generatedKeys.getInt(0)
        } else {
            throw SqlINSERTException(from = "JoseVoiceSchema.insert")
        }
    }

    suspend fun selectAll(): List<JoseVoiceLog> = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_ALL)
        val resultSet = statement.executeQuery()
        val list = mutableListOf<JoseVoiceLog>()
        while (resultSet.next()) {
            val id = resultSet.getInt(1)
            val timestamp = resultSet.getTimestamp(2).toLocalDateTime()

            val newlog = JoseVoiceLogFactory.build(id, timestamp.toKotlinLocalDateTime())
            list.add(newlog)
        }

        return@withContext list
    }
}