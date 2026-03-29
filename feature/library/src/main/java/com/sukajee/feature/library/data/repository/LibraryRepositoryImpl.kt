package com.sukajee.feature.library.data.repository

import com.sukajee.core.common.model.Book
import com.sukajee.core.common.utils.SampleData
import com.sukajee.core.database.dao.BookDao
import com.sukajee.core.database.mapper.toDomain
import com.sukajee.feature.library.domain.repository.LibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlin.collections.filter

class LibraryRepositoryImpl(private val bookDao: BookDao) : LibraryRepository {

    private val sampleFlow = MutableStateFlow(SampleData.books)

    override fun getAllBooks(): Flow<List<Book>> =
        bookDao.getAllBooks().map { entities ->
            if (entities.isEmpty()) SampleData.books
            else entities.map { it.toDomain() }
        }

    override fun searchBooks(query: String): Flow<List<Book>> =
        getAllBooks().map { books ->
            if (query.isBlank()) books
            else books.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.subject.contains(query, ignoreCase = true)
            }
        }

    override fun getBooksBySubject(subject: String): Flow<List<Book>> =
        getAllBooks().map { books ->
            if (subject == "All") books
            else books.filter { it.subject == subject }
        }

    override suspend fun toggleBookmark(bookId: String) {
        val book = bookDao.getBookById(bookId)
        if (book != null) {
            bookDao.updateBookmark(bookId, !book.isBookmarked)
        }
    }

    override suspend fun getSubjects(): List<String> =
        listOf("All") + SampleData.books.map { it.subject }.distinct().sorted()
}
