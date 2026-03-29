package com.sukajee.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sukajee.core.database.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("SELECT * FROM books ORDER BY title ASC")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE status = 'IN_PROGRESS' ORDER BY title ASC")
    fun getBooksInProgress(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE status = 'COMPLETED' ORDER BY title ASC")
    fun getCompletedBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: String): BookEntity?

    @Query("SELECT * FROM books WHERE status != 'COMPLETED' LIMIT 6")
    fun getRecommendedBooks(): Flow<List<BookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Update
    suspend fun updateBook(book: BookEntity)

    @Query("UPDATE books SET current_page = :page, status = :status WHERE id = :bookId")
    suspend fun updateReadingProgress(bookId: String, page: Int, status: String)

    @Query("UPDATE books SET is_bookmarked = :isBookmarked WHERE id = :bookId")
    suspend fun updateBookmark(bookId: String, isBookmarked: Boolean)

    @Query("DELETE FROM books")
    suspend fun deleteAllBooks()
}
