package campagnolo.cantiero.movieapp.utils

import campagnolo.cantiero.movieapp.model.AppDatabase
import campagnolo.cantiero.movieapp.model.Movie
import campagnolo.cantiero.movieapp.services.dao.MovieDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun populateDB(database: AppDatabase?) {
    database?.let { db ->
        withContext(Dispatchers.IO) {
            val movieDao: MovieDAO = db.movieDao()

            if (movieDao.getAll().isEmpty()) {
                val movies = listOf<Movie>(
                    Movie(name = "The Big Short", description = "great movie"),
                    Movie(name = "Arrival", description = "great movie"),
                    Movie(name = "Blade Runner 2049", description = "great movie"),
                    Movie(name = "Passengers", description = "great movie")
                )

                movieDao.save(movies)
            }
        }
    }
}