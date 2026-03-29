package com.sukajee.feature.reader.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sukajee.core.common.utils.Resource
import com.sukajee.feature.reader.domain.repository.ReaderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReaderViewModel(
    private val repository: ReaderRepository,
    private val bookId: String
) : ViewModel() {

    private val _state = MutableStateFlow(ReaderState(isLoading = true))
    val state: StateFlow<ReaderState> = _state.asStateFlow()

    init {
        loadBook()
    }

    private fun loadBook() {
        viewModelScope.launch {
            when (val result = repository.getBook(bookId)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            book = result.data,
                            currentPage = result.data.currentPage.coerceAtLeast(1),
                            isBookmarked = result.data.isBookmarked,
                            isLoading = false
                        )
                    }
                }
                is Resource.Error -> _state.update { it.copy(error = result.message, isLoading = false) }
                is Resource.Loading -> Unit
            }
        }
    }

    fun onEvent(event: ReaderEvent) {
        when (event) {
            is ReaderEvent.PageChanged -> {
                val book = _state.value.book ?: return
                val page = event.page.coerceIn(1, book.totalPages)
                _state.update { it.copy(currentPage = page) }
                viewModelScope.launch { repository.updateReadingProgress(bookId, page) }
            }
            is ReaderEvent.NextPage -> {
                val book = _state.value.book ?: return
                val next = (_state.value.currentPage + 1).coerceAtMost(book.totalPages)
                onEvent(ReaderEvent.PageChanged(next))
            }
            is ReaderEvent.PreviousPage -> {
                val prev = (_state.value.currentPage - 1).coerceAtLeast(1)
                onEvent(ReaderEvent.PageChanged(prev))
            }
            is ReaderEvent.ToggleBookmark -> {
                _state.update { it.copy(isBookmarked = !it.isBookmarked) }
                viewModelScope.launch { repository.toggleBookmark(bookId) }
            }
            is ReaderEvent.ToggleControls -> _state.update { it.copy(showControls = !it.showControls) }
            is ReaderEvent.IncreaseFontSize -> _state.update { it.copy(fontSize = (it.fontSize + 2).coerceAtMost(24)) }
            is ReaderEvent.DecreaseFontSize -> _state.update { it.copy(fontSize = (it.fontSize - 2).coerceAtLeast(12)) }
            is ReaderEvent.ToggleDarkMode -> _state.update { it.copy(isDarkMode = !it.isDarkMode) }
        }
    }
}
