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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen() {
    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    var groups by remember { mutableStateOf<List<GroupDto>>(emptyList()) }
    var selectedGroup by remember { mutableStateOf<GroupDto?>(null) }
    var groupsLoading by remember { mutableStateOf(true) }

    var previousSelectedGroup by remember { mutableStateOf<String?>(null) }

    // Загрузка групп (только один раз)
    LaunchedEffect(Unit) {
        try {
            println("🔄 Загрузка списка групп...")
            val groupsList = RetrofitInstance.api.getGroups()
            println("✅ Групп загружено: ${groupsList.size}")
            groups = groupsList
            selectedGroup = groupsList.find { it.name == "ИС-12" } ?: groupsList.firstOrNull()
            previousSelectedGroup = selectedGroup?.name
        } catch (e: Exception) {
            println("❌ Ошибка загрузки групп: ${e.message}")
            error = "Не удалось загрузить список групп"
        } finally {
            groupsLoading = false
            loading = false
        }
    }

    // Загрузка расписания (только при изменении группы)
    LaunchedEffect(selectedGroup?.name) {
        val currentGroupName = selectedGroup?.name
        if (currentGroupName != null && currentGroupName != previousSelectedGroup && !groupsLoading) {
            println("🔄 Загрузка расписания для: $currentGroupName")
            loading = true
            error = null

            try {
                val (start, end) = getWeekDateRange()
                schedule = RetrofitInstance.api.getSchedule(
                    groupName = currentGroupName,
                    start = start,
                    end = end
                )
                println("✅ Расписание загружено: ${schedule.size} дней")
                previousSelectedGroup = currentGroupName
            } catch (e: Exception) {
                println("❌ Ошибка: ${e.message}")
                error = e.message
            } finally {
                loading = false
            }
        }
    }

    Column(
        modifier = Modifier
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
                    println("📝 Выбор группы: ${group.name}")
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