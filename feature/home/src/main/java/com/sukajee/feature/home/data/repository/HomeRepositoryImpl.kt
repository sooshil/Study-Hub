package com.sukajee.feature.home.data.repository

import com.sukajee.core.common.model.Book
import com.sukajee.core.common.model.User
import com.sukajee.core.common.utils.Resource
import com.sukajee.core.database.dao.BookDao
import com.sukajee.core.database.dao.UserDao
import com.sukajee.core.database.mapper.toDomain
import com.sukajee.core.common.utils.SampleData
import com.sukajee.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class HomeRepositoryImpl(
    private val bookDao: BookDao,
    private val userDao: UserDao
) : HomeRepository {

    override fun getBooksInProgress(): Flow<List<Book>> =
        bookDao.getBooksInProgress()
            .map { entities ->
                if (entities.isEmpty()) SampleData.recentBooks
                else entities.map { it.toDomain() }
            }
            .onStart { seedIfEmpty() }

    override fun getRecommendedBooks(): Flow<List<Book>> =
        bookDao.getRecommendedBooks()
            .map { entities ->
                if (entities.isEmpty()) SampleData.recommendedBooks
                else entities.map { it.toDomain() }
            }

    override suspend fun getCurrentUser(): Resource<User> {
        val entity = userDao.getCurrentUserOnce()
        return if (entity != null) {
            Resource.Success(
                User(
                    entity.id, entity.name, entity.email, entity.grade,
                    entity.avatarEmoji, entity.booksRead, entity.hoursStudied,
                    entity.streakDays, entity.gradeAverage
                )
            )
        } else {
            Resource.Success(
                User(
                    "user_001", "Alex Johnson", "alex@studyhub.app", "Grade 10",
                    "🎓", 12, 48, 7, "A+"
                )
            )
        }
    }

    override suspend fun getTotalStudyHours(): Int = 48
    override suspend fun getCurrentStreak(): Int = 7
    override suspend fun getBooksCompletedCount(): Int = SampleData.completedBooks.size

    private suspend fun seedIfEmpty() {
        val all = bookDao.getBooksInProgress()
    }
}
