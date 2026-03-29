package com.sukajee.feature.library.presentation

import com.sukajee.core.common.model.Book

data class LibraryState(
    val books: List<Book> = emptyList(),
    val filteredBooks: List<Book> = emptyList(),
    val subjects: List<String> = listOf("All"),
    val selectedSubject: String = "All",
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isGridView: Boolean = true
)

sealed interface LibraryEvent {
    data class SearchQueryChanged(val query: String) : LibraryEvent
    data class SubjectSelected(val subject: String) : LibraryEvent
    data object ToggleViewMode : LibraryEvent
    data class BookClicked(val bookId: String) : LibraryEvent
    data class ToggleBookmark(val bookId: String) : LibraryEvent
}
