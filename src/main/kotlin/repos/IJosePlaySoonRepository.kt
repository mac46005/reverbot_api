package com.example.repos

import com.example.models.JosePlaySoonLog
import kotlinx.datetime.LocalDateTime

interface IJosePlaySoonRepository {
    suspend fun create(timestamp: LocalDateTime): Int
    suspend fun getAll(): List<JosePlaySoonLog>
}