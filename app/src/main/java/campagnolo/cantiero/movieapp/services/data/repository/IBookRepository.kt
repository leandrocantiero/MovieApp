package campagnolo.cantiero.movieapp.services.data.repository

import campagnolo.cantiero.movieapp.services.data.entity.Book
import campagnolo.cantiero.movieapp.services.api.listener.ApiListener

interface IBookRepository {
    suspend fun save(book: Book)

    suspend fun save(books: List<Book>)

    suspend fun getAll(): List<Book>

    suspend fun getByTitle(title: String): List<Book>

    suspend fun remove(book: Book)

    fun getAllFromApi(listener: ApiListener)
}