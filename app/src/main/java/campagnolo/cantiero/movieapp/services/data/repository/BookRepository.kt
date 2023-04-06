package campagnolo.cantiero.movieapp.services.data.repository

import android.content.Context
import campagnolo.cantiero.movieapp.services.data.AppDatabase
import campagnolo.cantiero.movieapp.services.data.entity.Book
import campagnolo.cantiero.movieapp.services.api.model.BookBodyResponse
import campagnolo.cantiero.movieapp.services.api.APIService
import campagnolo.cantiero.movieapp.services.api.listener.ApiListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookRepository(val context: Context): IBookRepository {
    private val bookDao = AppDatabase.getDatabase(context).bookDao()

    override suspend fun save(book: Book) {
        bookDao.save(book)
    }

    override suspend fun save(books: List<Book>) {
        bookDao.save(books)
    }

    override suspend fun getAll(): List<Book> {
        return bookDao.getAll()
    }

    override suspend fun getByTitle(title: String): List<Book> {
        return bookDao.getByTitle(title)
    }

    override suspend fun remove(book: Book) {
        bookDao.remove(book)
    }

    override fun getAllFromApi(listener: ApiListener) {
        APIService.bookService.getBooks().enqueue(object : Callback<BookBodyResponse> {
            override fun onResponse(
                call: Call<BookBodyResponse>, response: Response<BookBodyResponse>
            ) {
                if (response.isSuccessful) {
                    val books = arrayListOf<Book>()

                    for (res in response.body()?.results!!) {
                        val book = Book(
                            id = 0,
                            title = res.bookDetails[0].title,
                            description = res.bookDetails[0].description,
                            author = res.bookDetails[0].author,
                            amazonUrl = res.amazonUrl
                        )
                        books.add(book)
                    }

                    CoroutineScope(Dispatchers.IO).launch {
                        bookDao.removeAll()
                        bookDao.save(books)
                        listener.onSuccess()
                    }
                } else {
                    listener.onFail("Não foi possível completar a operação")
                }
            }

            override fun onFailure(call: Call<BookBodyResponse>, t: Throwable) {
                listener.onFail(t.message)
            }
        })
    }
}