package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.GroupDto
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.network.RetrofitInstance
import com.example.collegeschedule.utils.getWeekDateRange
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(modifier: Modifier = Modifier) {
    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    var groups by remember { mutableStateOf<List<GroupDto>>(emptyList()) }
    var selectedGroup by remember { mutableStateOf<GroupDto?>(null) }
    var groupsLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            println("[DEBUG] Загрузка списка групп...")
            groups = RetrofitInstance.api.getGroups()
            println("[DEBUG] Групп загружено: ${groups.size}")

            val savedGroup = RetrofitInstance.favoritesManager.selectedGroupFlow.first()
            println("[DEBUG] Сохраненная группа из DataStore: $savedGroup")

            selectedGroup = if (savedGroup != null) {
                groups.find { it.name == savedGroup }
            } else {
                groups.find { it.name == "ИС-12" }
            } ?: groups.firstOrNull()

            println("[DEBUG] Выбрана группа: ${selectedGroup?.name}")

        } catch (e: Exception) {
            println("[DEBUG] Ошибка загрузки групп: ${e.message}")
            e.printStackTrace()
            error = "Не удалось загрузить список групп"
        } finally {
            groupsLoading = false
            loading = false
        }
    }

    LaunchedEffect(selectedGroup?.name) {
        val groupName = selectedGroup?.name
        if (groupName != null && !groupsLoading) {
            println("[DEBUG] Начало загрузки расписания для: $groupName")
            loading = true
            error = null
            try {
                val (start, end) = getWeekDateRange()
                schedule = RetrofitInstance.api.getSchedule(
                    groupName = groupName,
                    start = start,
                    end = end
                )
                println("[DEBUG] Расписание загружено: ${schedule.size} дней")

                println("[DEBUG] Сохранение группы в DataStore: $groupName")
                RetrofitInstance.favoritesManager.saveSelectedGroup(groupName)
                println("[DEBUG] Группа сохранена в DataStore")

            } catch (e: Exception) {
                println("[DEBUG] Ошибка при загрузке расписания: ${e.message}")
                e.printStackTrace()
                error = e.message
            } finally {
                loading = false
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Загружено групп: ${groups.size}",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (groupsLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            GroupDropdown(
                groups = groups,
                selectedGroup = selectedGroup,
                onGroupSelected = { group ->
                    println("[DEBUG] Пользователь выбрал группу: ${group.name}")
                    selectedGroup = group
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            loading && schedule.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            error != null && schedule.isEmpty() -> {
                Text(
                    text = "Ошибка: $error",
                    color = MaterialTheme.colorScheme.error
                )
            }
            else -> {
                if (schedule.isNotEmpty()) {
                    ScheduleList(schedule)
                }
            }
        }
    }
}