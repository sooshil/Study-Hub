package com.sukajee.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sukajee.core.common.utils.Resource
import com.sukajee.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _state.update { it.copy(isLoading = true) }

        repository.getBooksInProgress()
            .onEach { books -> _state.update { it.copy(booksInProgress = books) } }
            .launchIn(viewModelScope)

        repository.getRecommendedBooks()
            .onEach { books -> _state.update { it.copy(recommendedBooks = books) } }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            when (val userResult = repository.getCurrentUser()) {
                is Resource.Success -> _state.update { it.copy(user = userResult.data) }
                else -> Unit
            }
            val hours = repository.getTotalStudyHours()
            val streak = repository.getCurrentStreak()
            val completed = repository.getBooksCompletedCount()
            _state.update {
                it.copy(
                    studyHours = hours,
                    streak = streak,
                    booksCompleted = completed,
                    isLoading = false
                )
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.Refresh -> loadData()
            is HomeEvent.BookClicked -> Unit
        }
    }
}
