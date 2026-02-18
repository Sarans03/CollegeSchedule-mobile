package com.example.collegeschedule.data.repository

import com.example.collegeschedule.data.api.ScheduleApi
import com.example.collegeschedule.data.dto.GroupDto
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.local.FavoritesManager
import kotlinx.coroutines.flow.Flow

class ScheduleRepository(
    private val api: ScheduleApi,
    private val favoritesManager: FavoritesManager
) {

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

    suspend fun loadGroups(): List<GroupDto> {
        return api.getGroups()
    }

    val selectedGroupFlow: Flow<String?> = favoritesManager.selectedGroupFlow

    suspend fun saveSelectedGroup(groupName: String) {
        favoritesManager.saveSelectedGroup(groupName)
    }

    suspend fun clearSelectedGroup() {
        favoritesManager.clearSelectedGroup()
    }
}