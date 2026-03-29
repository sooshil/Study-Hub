package com.sukajee.feature.library.domain.repository

import com.sukajee.core.common.model.Book
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    fun getAllBooks(): Flow<List<Book>>
    fun searchBooks(query: String): Flow<List<Book>>
    fun getBooksBySubject(subject: String): Flow<List<Book>>
    suspend fun toggleBookmark(bookId: String)
    suspend fun getSubjects(): List<String>
}