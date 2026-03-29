package com.sukajee.feature.profile.data.repository

import com.sukajee.core.common.model.User
import com.sukajee.core.common.utils.Resource
import com.sukajee.core.database.dao.UserDao
import com.sukajee.feature.profile.domain.repository.ProfileRepository
import com.sukajee.feature.auth.data.local.AuthPreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileRepositoryImpl(
    private val userDao: UserDao,
    private val authPrefsManager: AuthPreferencesManager
) : ProfileRepository {

    override fun getUserProfile(): Flow<User> =
        userDao.getCurrentUser().map { entity ->
            entity?.let {
                User(
                    it.id, it.name, it.email, it.grade,
                    it.avatarEmoji, it.booksRead, it.hoursStudied,
                    it.streakDays, it.gradeAverage
                )
            } ?: User(
                id = "user_001",
                name = "Alex Johnson",
                email = "alex@studyhub.app",
                grade = "Grade 10",
                avatarEmoji = "🎓",
                booksRead = 12,
                hoursStudied = 48,
                streakDays = 7,
                gradeAverage = "A+"
            )
        }

    override suspend fun updateProfile(name: String, grade: String): Resource<User> {
        return Resource.Success(
            User("user_001", name, "alex@studyhub.app", grade)
        )
    }

    override suspend fun logout() {
        userDao.deleteAllUsers()
        authPrefsManager.clearAuthData()
    }
}
