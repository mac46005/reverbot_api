package com.example.repos

import com.example.data.CreateJosePlaySoonRequest
import com.example.data.JosePlaySoonSchema
import com.example.models.JosePlaySoonLog
import kotlinx.datetime.LocalDateTime

class DefaultJosePlaySoonRepository(
    private val josePlaySoonSchema: JosePlaySoonSchema
): IJosePlaySoonRepository {
    override suspend fun create(timestamp: LocalDateTime): Int {
        return josePlaySoonSchema.insert(CreateJosePlaySoonRequest(timestamp))
    }

    override suspend fun getAll(): List<JosePlaySoonLog> {
        return josePlaySoonSchema.selectAll()
    }
}