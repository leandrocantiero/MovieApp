package campagnolo.cantiero.movieapp

import androidx.test.core.app.ApplicationProvider
import campagnolo.cantiero.movieapp.model.AppDatabase
import campagnolo.cantiero.movieapp.model.Movie
import campagnolo.cantiero.movieapp.services.dao.MovieDAO
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MovieDAOTest {
    private lateinit var database: AppDatabase
    private lateinit var movieDao: MovieDAO

    @Before
    fun setup() {
        database = AppDatabase.getInMemoryDatabase(ApplicationProvider.getApplicationContext())
        movieDao = database.movieDao()
    }

    @Test
    fun save() = runBlocking {
        val movie = Movie(0, "Nome do filme", "Descrição do filme", 10.0, "")
        movieDao.save(movie)

        val result = movieDao.getAll()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Nome do filme", result[0].name)
    }

    @Test
    fun remove() = runBlocking {
        val movie = Movie(0, "Nome do filme", "Descrição do filme", 10.0, "")
        movieDao.save(movie)
        movieDao.remove(movie)

        val result = movieDao.getAll()

        Assert.assertEquals(0, result.size)
        Assert.assertEquals("Nome do filme", result[0].name)
    }

    @After
    fun tearDown() {
        database.close()
    }
}