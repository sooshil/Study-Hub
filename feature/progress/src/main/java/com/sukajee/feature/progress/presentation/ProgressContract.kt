package com.sukajee.feature.progress.presentation

import com.sukajee.feature.progress.domain.model.StudyStats
import com.sukajee.feature.progress.domain.model.SubjectProgress
import com.sukajee.feature.progress.domain.model.WeeklyStudyData


data class ProgressState(
    val weeklyData: List<WeeklyStudyData> = emptyList(),
    val subjectProgress: List<SubjectProgress> = emptyList(),
    val stats: StudyStats? = null,
    val isLoading: Boolean = false
)

sealed interface ProgressEvent {
    data object Refresh : ProgressEvent
}