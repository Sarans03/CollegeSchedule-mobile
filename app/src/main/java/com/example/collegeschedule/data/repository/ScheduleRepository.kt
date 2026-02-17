package com.example.collegeschedule.data.repository

import com.example.collegeschedule.data.api.ScheduleApi
import com.example.collegeschedule.data.dto.ScheduleByDateDto

class ScheduleRepository(private val api: ScheduleApi) {
    suspend fun loadSchedule(
        group: String,
        start: String = "2026-02-09",
        end: String = "2026-03-11"
    ): List<ScheduleByDateDto> {
        return api.getSchedule(
            groupName = group,
            start = start,
            end = end
        )
    }
}