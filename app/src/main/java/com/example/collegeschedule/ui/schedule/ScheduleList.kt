package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.ui.theme.BrownLight
import com.example.collegeschedule.ui.theme.BrownPrimary
import com.example.collegeschedule.ui.theme.CreamBackground

@Composable
fun ScheduleList(data: List<ScheduleByDateDto>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(data, key = { it.lessonDate }) { day ->
            ScheduleDayCard(day)
        }
    }
}

@Composable
fun ScheduleDayCard(day: ScheduleByDateDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = CreamBackground
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${day.lessonDate} (${day.weekday})",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = BrownPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Divider(
                color = BrownLight.copy(alpha = 0.5f),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            if (day.lessons.isEmpty()) {
                Text(
                    text = "Нет пар",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BrownLight,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                day.lessons.forEach { lesson ->
                    PairCard(lesson)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun PairCard(lesson: com.example.collegeschedule.data.dto.LessonDto) {
    val cardBackground = Color(0xFFFFF9F0)
    val cardAccent = BrownPrimary

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(cardBackground),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardBackground
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Пара ${lesson.lessonNumber}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = cardAccent
                )

                Text(
                    text = lesson.time,
                    style = MaterialTheme.typography.bodySmall,
                    color = BrownLight
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            lesson.groupParts.forEach { (part, info) ->
                if (info != null) {
                    Text(
                        text = info.subject,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = BrownPrimary,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = " ${info.teacher}",
                        style = MaterialTheme.typography.bodySmall,
                        color = BrownLight,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )

                    Text(
                        text = " ${info.building}, ${info.classroom}",
                        style = MaterialTheme.typography.bodySmall,
                        color = BrownLight
                    )
                }
            }
        }
    }
}