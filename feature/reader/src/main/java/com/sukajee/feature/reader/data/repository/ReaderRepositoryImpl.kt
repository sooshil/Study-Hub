package com.sukajee.feature.reader.data.repository

import com.sukajee.core.common.model.Book
import com.sukajee.core.common.model.BookStatus
import com.sukajee.core.common.utils.Resource
import com.sukajee.core.common.utils.SampleData
import com.sukajee.core.database.dao.BookDao
import com.sukajee.core.database.mapper.toDomain
import com.sukajee.feature.reader.domain.repository.ReaderRepository

class ReaderRepositoryImpl(private val bookDao: BookDao) : ReaderRepository {

    override suspend fun getBook(bookId: String): Resource<Book> {
        val entity = bookDao.getBookById(bookId)
        return if (entity != null) {
            Resource.Success(entity.toDomain())
        } else {
            val sample = SampleData.books.find { it.id == bookId }
                ?: SampleData.books.first()
            Resource.Success(sample)
        }
    }

    override suspend fun updateReadingProgress(bookId: String, currentPage: Int) {
        val book = bookDao.getBookById(bookId) ?: return
        val status = when {
            currentPage >= book.totalPages -> BookStatus.COMPLETED.name
            currentPage > 0 -> BookStatus.IN_PROGRESS.name
            else -> BookStatus.NOT_STARTED.name
        }
        bookDao.updateReadingProgress(bookId, currentPage, status)
    }

    override suspend fun toggleBookmark(bookId: String) {
        val book = bookDao.getBookById(bookId) ?: return
        bookDao.updateBookmark(bookId, !book.isBookmarked)
    }
}
