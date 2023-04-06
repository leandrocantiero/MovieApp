package campagnolo.cantiero.movieapp.services.data.repository

import campagnolo.cantiero.movieapp.services.data.entity.Movie
import campagnolo.cantiero.movieapp.services.api.listener.ApiListener

interface IMovieRepository {
    suspend fun save(movie: Movie)

    suspend fun save(movies: List<Movie>)

    suspend fun getAll(): List<Movie>

    suspend fun getByName(name: String): List<Movie>

    suspend fun remove(movie: Movie)

    fun getAllFromApi(listener: ApiListener)
}