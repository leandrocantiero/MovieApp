package campagnolo.cantiero.movieapp.services.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import campagnolo.cantiero.movieapp.services.data.entity.Book

@Dao
interface BookDAO {
    @Upsert
    suspend fun save(book: Book)

    @Upsert
    suspend fun save(books: List<Book>)

    @Query("SELECT * FROM book")
    suspend fun getAll(): List<Book>

    @Query("SELECT * FROM book WHERE title like :title")
    suspend fun getByTitle(title: String): List<Book>

    @Delete
    suspend fun remove(book: Book)

    @Query("DELETE FROM book")
    suspend fun removeAll()
}