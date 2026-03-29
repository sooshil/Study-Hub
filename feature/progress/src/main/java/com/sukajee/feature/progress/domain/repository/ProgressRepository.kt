package com.sukajee.feature.progress.domain.repository

import com.sukajee.feature.progress.domain.model.StudyStats
import com.sukajee.feature.progress.domain.model.SubjectProgress
import com.sukajee.feature.progress.domain.model.WeeklyStudyData
import kotlinx.coroutines.flow.Flow

interface ProgressRepository {
    fun getWeeklyStudyData(): Flow<List<WeeklyStudyData>>
    fun getSubjectProgress(): Flow<List<SubjectProgress>>
    fun getStudyStats(): Flow<StudyStats>
}
