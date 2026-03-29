package com.sukajee.feature.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sukajee.core.common.model.Book
import com.sukajee.feature.library.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.filter

class LibraryViewModel(private val repository: LibraryRepository) : ViewModel() {

    private val _state = MutableStateFlow(LibraryState())
    val state: StateFlow<LibraryState> = _state.asStateFlow()

    init {
        loadSubjects()
        observeBooks()
    }

    private fun loadSubjects() {
        viewModelScope.launch {
            val subjects = repository.getSubjects()
            _state.update { it.copy(subjects = subjects) }
        }
    }

    private fun observeBooks() {
        repository.getAllBooks()
            .onEach { books ->
                _state.update { state ->
                    state.copy(
                        books = books,
                        filteredBooks = filterBooks(books, state.searchQuery, state.selectedSubject)
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun filterBooks(
        books: List<Book>,
        query: String,
        subject: String
    ) = books.filter { book ->
        val matchesQuery = query.isBlank() ||
                book.title.contains(query, ignoreCase = true) ||
                book.subject.contains(query, ignoreCase = true)
        val matchesSubject = subject == "All" || book.subject == subject
        matchesQuery && matchesSubject
    }

    fun onEvent(event: LibraryEvent) {
        when (event) {
            is LibraryEvent.SearchQueryChanged -> {
                _state.update { state ->
                    val filtered = filterBooks(state.books, event.query, state.selectedSubject)
                    state.copy(searchQuery = event.query, filteredBooks = filtered)
                }
            }
            is LibraryEvent.SubjectSelected -> {
                _state.update { state ->
                    val filtered = filterBooks(state.books, state.searchQuery, event.subject)
                    state.copy(selectedSubject = event.subject, filteredBooks = filtered)
                }
            }
            is LibraryEvent.ToggleViewMode -> {
                _state.update { it.copy(isGridView = !it.isGridView) }
            }
            is LibraryEvent.ToggleBookmark -> {
                viewModelScope.launch { repository.toggleBookmark(event.bookId) }
            }
            is LibraryEvent.BookClicked -> Unit
        }
    }
}
