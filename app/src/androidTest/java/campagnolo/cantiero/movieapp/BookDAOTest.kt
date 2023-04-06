package campagnolo.cantiero.movieapp

import androidx.test.core.app.ApplicationProvider
import campagnolo.cantiero.movieapp.services.data.AppDatabase
import campagnolo.cantiero.movieapp.services.data.entity.Book
import campagnolo.cantiero.movieapp.services.data.dao.BookDAO
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class BookDAOTest {
    private lateinit var database: AppDatabase
    private lateinit var bookDao: BookDAO

    @Before
    fun setup() {
        database = AppDatabase.getInMemoryDatabase(ApplicationProvider.getApplicationContext())
        bookDao = database.bookDao()
    }

    @Test
    fun save() = runBlocking {
        val book = Book(
            0,
            "Nome do livro",
            "Descrição do livro",
            "autor teste",
            "https://link.com.br/livro"
        )

        bookDao.save(book)

        val result = bookDao.getAll()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Nome do livro", result[0].title)
    }

    @Test
    fun remove() = runBlocking {
        val book = Book(
            0,
            "Nome do livro",
            "Descrição do livro",
            "autor teste",
            "https://link.com.br/livro"
        )
        bookDao.save(book)
        bookDao.remove(book)

        val result = bookDao.getAll()

        Assert.assertEquals(0, result.size)
        Assert.assertEquals("Nome do livro", result[0].title)
    }

    @After
    fun tearDown() {
        database.close()
    }
}