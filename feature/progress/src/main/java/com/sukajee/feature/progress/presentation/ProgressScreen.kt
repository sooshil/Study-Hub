package com.sukajee.feature.progress.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sukajee.core.design.components.ProgressRing
import com.sukajee.core.design.components.StatsCard
import com.sukajee.core.design.theme.Indigo500
import com.sukajee.core.design.theme.Purple500
import com.sukajee.feature.progress.domain.model.WeeklyStudyData
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProgressScreen(viewModel: ProgressViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(colors = listOf(Indigo500, Purple500)))
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 28.dp)
            ) {
                Column {
                    Text("Study Progress", style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Track your learning journey", style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f))

                    state.stats?.let { stats ->
                        Spacer(modifier = Modifier.height(20.dp))
                        // Weekly goal ring
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            ProgressRing(
                                progress = stats.weeklyGoalProgress,
                                color = Color.White,
                                size = 80.dp,
                                strokeWidth = 7.dp
                            )
                            Column {
                                Text("Weekly Goal", style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold, color = Color.White)
                                Text("${(stats.weeklyGoalProgress * stats.weeklyGoalHours).toInt()}h of ${stats.weeklyGoalHours}h studied",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(0.8f))
                                Text("${7 - (stats.weeklyGoalProgress * 7).toInt()} days remaining",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(0.6f))
                            }
                        }
                    }
                }
            }
        }

        state.stats?.let { stats ->
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatsCard(
                        value = "${stats.totalHours}h",
                        label = "Total Hours",
                        icon = Icons.Filled.Schedule,
                        iconTint = Color(0xFF6366f1),
                        iconBackground = Color(0xFF6366f1).copy(0.12f),
                        modifier = Modifier.weight(1f)
                    )
                    StatsCard(
                        value = "${stats.currentStreak}",
                        label = "Day Streak",
                        icon = Icons.Filled.LocalFireDepartment,
                        iconTint = Color(0xFFf59e0b),
                        iconBackground = Color(0xFFf59e0b).copy(0.12f),
                        modifier = Modifier.weight(1f)
                    )
                    StatsCard(
                        value = stats.gradeAverage,
                        label = "Average",
                        icon = Icons.Filled.Star,
                        iconTint = Color(0xFF10b981),
                        iconBackground = Color(0xFF10b981).copy(0.12f),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        if (state.weeklyData.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("This Week", style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("${state.weeklyData.sumOf { it.hours.toDouble() }.let { "%.1f".format(it) }}h total",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(16.dp))
                        WeeklyBarChart(data = state.weeklyData)
                    }
                }
            }
        }

        if (state.subjectProgress.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("By Subject", style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(state.subjectProgress) { subject ->
                SubjectProgressItem(
                    subject = subject,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun WeeklyBarChart(data: List<WeeklyStudyData>) {
    val maxHours = data.maxOfOrNull { it.hours } ?: 1f
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = "${item.hours}h",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (item.isToday) Color(0xFF6366f1)
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (item.isToday) FontWeight.Bold else FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height((item.hours / maxHours * 80).dp.coerceAtLeast(4.dp))
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .background(
                            if (item.isToday) Color(0xFF6366f1)
                            else Color(0xFF6366f1).copy(alpha = 0.3f)
                        )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.day,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (item.isToday) Color(0xFF6366f1)
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (item.isToday) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun SubjectProgressItem(
    subject: com.studyhub.feature.progress.domain.model.SubjectProgress,
    modifier: Modifier = Modifier
) {
    val color = hexToColor(subject.accentColorHex)
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color.copy(0.12f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(subject.emoji, style = MaterialTheme.typography.titleMedium)
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(subject.subject, style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold)
                    Text("${(subject.progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold, color = color)
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { subject.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = color,
                    trackColor = color.copy(0.15f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("${subject.completedPages} / ${subject.totalPages} pages",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
