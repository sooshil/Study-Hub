package com.sukajee.feature.auth.domain.usecases

import com.sukajee.feature.auth.domain.repository.AuthRepository

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}
