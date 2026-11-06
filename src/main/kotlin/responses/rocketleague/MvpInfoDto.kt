package com.example.responses.rocketleague

import com.example.models.rocketleague.MvpInfo
import kotlinx.serialization.Serializable

@Serializable
data class MvpInfoDto(
    val mvp: String,
    val mvpCount: Long,
    val mvpRation: Double
)


fun MvpInfo.toDto(): MvpInfoDto {
    return MvpInfoDto(
        mvp = this.mvp,
        mvpCount = this.mvpCount,
        mvpRation = this.mvpRation
    )
}