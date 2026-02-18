package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.collegeschedule.data.dto.GroupDto

@Composable
fun GroupDropdown(
    groups: List<GroupDto>,
    selectedGroup: GroupDto?,
    onGroupSelected: (GroupDto) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredGroups = groups.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    // Поле выбора группы
    OutlinedTextField(
        value = selectedGroup?.name ?: "",
        onValueChange = {},
        readOnly = true,
        label = { Text("Выберите группу") },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Открыть список групп"
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .clickable { showDialog = true }
    )

    // Диалог с поиском и списком
    if (showDialog) {
        Dialog(onDismissRequest = {
            showDialog = false
            searchQuery = ""
        }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Выберите группу",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Поле поиска
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Поиск") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Список групп
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    ) {
                        items(filteredGroups, key = { it.id }) { group ->
                            ListItem(
                                headlineContent = { Text(group.name) },
                                modifier = Modifier
                                    .clickable {
                                        onGroupSelected(group)
                                        showDialog = false
                                        searchQuery = ""
                                    }
                                    .fillMaxWidth()
                            )
                        }

                        if (filteredGroups.isEmpty() && searchQuery.isNotEmpty()) {
                            item {
                                Text(
                                    "Группы не найдены",
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}