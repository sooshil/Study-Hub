package com.sukajee.feature.home.domain.repository

import com.sukajee.core.common.model.Book
import com.sukajee.core.common.model.User
import com.sukajee.core.common.utils.Resource
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getBooksInProgress(): Flow<List<Book>>
    fun getRecommendedBooks(): Flow<List<Book>>
    suspend fun getCurrentUser(): Resource<User>
    suspend fun getTotalStudyHours(): Int
    suspend fun getCurrentStreak(): Int
    suspend fun getBooksCompletedCount(): Int
}
