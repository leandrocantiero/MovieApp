package campagnolo.cantiero.movieapp.services.repository

import campagnolo.cantiero.movieapp.model.Movie
import campagnolo.cantiero.movieapp.services.api.listener.MovieListener

interface IMovieRepository {
    suspend fun save(movie: Movie)

    suspend fun save(movies: List<Movie>)

    suspend fun getAll(): List<Movie>

    suspend fun getByName(name: String): List<Movie>

    suspend fun remove(movie: Movie)

    suspend fun getAllFromApi(listener: MovieListener)
}