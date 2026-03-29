package com.sukajee.feature.progress.data.repository

import com.sukajee.core.database.dao.BookDao
import com.sukajee.feature.progress.domain.model.StudyStats
import com.sukajee.feature.progress.domain.model.SubjectProgress
import com.sukajee.feature.progress.domain.model.WeeklyStudyData
import com.sukajee.feature.progress.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class ProgressRepositoryImpl(private val bookDao: BookDao) : ProgressRepository {

    override fun getWeeklyStudyData(): Flow<List<WeeklyStudyData>> = flowOf(
        listOf(
            WeeklyStudyData("Mon", 1.5f),
            WeeklyStudyData("Tue", 2.0f),
            WeeklyStudyData("Wed", 3.5f),
            WeeklyStudyData("Thu", 2.5f),
            WeeklyStudyData("Fri", 4.0f),
            WeeklyStudyData("Sat", 1.0f),
            WeeklyStudyData("Sun", 2.0f, isToday = true)
        )
    )

    override fun getSubjectProgress(): Flow<List<SubjectProgress>> =
        bookDao.getAllBooks().map { entities ->
            if (entities.isEmpty()) {
                listOf(
                    SubjectProgress("Mathematics", "📐", 0.60f, "#6366f1", 180, 300),
                    SubjectProgress("Physics", "⚛️", 0.20f, "#8b5cf6", 50, 250),
                    SubjectProgress("English", "📖", 0.40f, "#10b981", 80, 200),
                    SubjectProgress("History", "🌍", 1.00f, "#3b82f6", 400, 400),
                    SubjectProgress("Geography", "🗺️", 0.50f, "#84cc16", 120, 240)
                )
            } else {
                entities
                    .groupBy { it.subject }
                    .map { (subject, books) ->
                        val totalPages = books.sumOf { it.totalPages }
                        val currentPage = books.sumOf { it.currentPage }
                        val progress = if (totalPages > 0) currentPage.toFloat() / totalPages else 0f
                        val book = books.first()
                        SubjectProgress(
                            subject = subject,
                            emoji = book.emoji,
                            progress = progress,
                            accentColorHex = book.accentColorHex,
                            completedPages = currentPage,
                            totalPages = totalPages
                        )
                    }
            }
        }

    override fun getStudyStats(): Flow<StudyStats> = flowOf(
        StudyStats(
            totalHours = 48,
            currentStreak = 7,
            booksCompleted = 2,
            gradeAverage = "A+",
            weeklyGoalProgress = 0.65f,
            weeklyGoalHours = 20
        )
    )
}
