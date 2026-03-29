package com.sukajee.feature.home.presentation

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sukajee.core.design.components.BookGridCard
import com.sukajee.core.design.components.BookListCard
import com.sukajee.core.design.components.StatsCard
import com.sukajee.core.design.theme.Indigo500
import com.sukajee.core.design.theme.Purple500
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onBookClick: (String) -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            // Gradient header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(colors = listOf(Indigo500, Purple500))
                    )
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 28.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = state.user?.avatarEmoji ?: "🎓",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Good morning,",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text = state.user?.name?.substringBefore(" ") ?: "Student",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${state.user?.grade ?: "Grade 10"} · ${state.streak} day streak 🔥",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }

        item {
            // Stats row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatsCard(
                    value = "${state.studyHours}h",
                    label = "Hours Studied",
                    icon = Icons.Filled.Schedule,
                    iconTint = Color(0xFF6366f1),
                    iconBackground = Color(0xFF6366f1).copy(alpha = 0.12f),
                    subtitle = "+5h this week",
                    modifier = Modifier.weight(1f)
                )
                StatsCard(
                    value = "${state.streak}",
                    label = "Day Streak",
                    icon = Icons.Filled.LocalFireDepartment,
                    iconTint = Color(0xFFf59e0b),
                    iconBackground = Color(0xFFf59e0b).copy(alpha = 0.12f),
                    subtitle = "Keep it up!",
                    modifier = Modifier.weight(1f)
                )
                StatsCard(
                    value = "${state.booksCompleted}",
                    label = "Completed",
                    icon = Icons.Filled.AutoStories,
                    iconTint = Color(0xFF10b981),
                    iconBackground = Color(0xFF10b981).copy(alpha = 0.12f),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        if (state.booksInProgress.isNotEmpty()) {
            item {
                SectionHeader(
                    title = "Continue Reading",
                    subtitle = "${state.booksInProgress.size} books in progress",
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
            items(state.booksInProgress.take(3)) { book ->
                BookListCard(
                    emoji = book.emoji,
                    title = book.title,
                    subject = book.subject,
                    progress = book.progress,
                    accentColor = hexToColor(book.accentColorHex),
                    onClick = { onBookClick(book.id) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }

        if (state.recommendedBooks.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                SectionHeader(
                    title = "Recommended for You",
                    subtitle = "Based on your grade",
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.recommendedBooks) { book ->
                        BookGridCard(
                            emoji = book.emoji,
                            title = book.title,
                            grade = book.grade,
                            progress = book.progress,
                            accentColor = hexToColor(book.accentColorHex),
                            onClick = { onBookClick(book.id) },
                            modifier = Modifier.width(150.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(bottom = 8.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

internal fun hexToColor(hex: String): Color {
    return try {
        val cleaned = hex.removePrefix("#")
        Color(android.graphics.Color.parseColor("#$cleaned"))
    } catch (e: Exception) {
        Color(0xFF6366f1)
    }
}
