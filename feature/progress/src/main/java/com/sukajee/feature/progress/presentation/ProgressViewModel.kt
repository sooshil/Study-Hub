package com.sukajee.feature.progress.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sukajee.feature.progress.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ProgressViewModel(private val repository: ProgressRepository) : ViewModel() {

    private val _state = MutableStateFlow(ProgressState(isLoading = true))
    val state: StateFlow<ProgressState> = _state.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        repository.getWeeklyStudyData()
            .onEach { data -> _state.update { it.copy(weeklyData = data) } }
            .launchIn(viewModelScope)

        repository.getSubjectProgress()
            .onEach { subjects -> _state.update { it.copy(subjectProgress = subjects) } }
            .launchIn(viewModelScope)

        repository.getStudyStats()
            .onEach { stats -> _state.update { it.copy(stats = stats, isLoading = false) } }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: ProgressEvent) {
        when (event) {
            is ProgressEvent.Refresh -> loadData()
        }
    }
}
