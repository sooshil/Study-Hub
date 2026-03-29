package com.sukajee.feature.home.presentation

import com.sukajee.core.common.model.Book
import com.sukajee.core.common.model.User


data class HomeState(
    val user: User? = null,
    val booksInProgress: List<Book> = emptyList(),
    val recommendedBooks: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val studyHours: Int = 0,
    val streak: Int = 0,
    val booksCompleted: Int = 0
)

sealed interface HomeEvent {
    data object Refresh : HomeEvent
    data class BookClicked(val bookId: String) : HomeEvent
}
