package campagnolo.cantiero.movieapp.services.data.repository

import android.content.Context
import campagnolo.cantiero.movieapp.services.data.AppDatabase
import campagnolo.cantiero.movieapp.services.api.model.MovieBodyResponse
import campagnolo.cantiero.movieapp.services.data.entity.Movie
import campagnolo.cantiero.movieapp.services.api.APIService
import campagnolo.cantiero.movieapp.services.api.listener.ApiListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRepository(val context: Context) : IMovieRepository {
    private val movieDao = AppDatabase.getDatabase(context).movieDao()

    override suspend fun save(movie: Movie) {
        return movieDao.save(movie)
    }

    override suspend fun save(movies: List<Movie>) {
        return movieDao.save(movies)
    }

    override suspend fun getAll(): List<Movie> {
        return movieDao.getAll()
    }

    override suspend fun getByName(name: String): List<Movie> {
        val search = "%${name}%"
        return movieDao.getByName(search)
    }

    override suspend fun remove(movie: Movie) {
        return movieDao.remove(movie)
    }

    override fun getAllFromApi(listener: ApiListener) {
        APIService.movieService.getMovies().enqueue(object : Callback<MovieBodyResponse> {
            override fun onResponse(
                call: Call<MovieBodyResponse>, response: Response<MovieBodyResponse>
            ) {
                if (response.isSuccessful) {
                    val movies = arrayListOf<Movie>()

                    for (res in response.body()?.results!!) {
                        val movie = Movie(
                            name = res.displayTitle,
                            description = res.summaryShort,
                            rating = 0.0,
                            image = res.multimedia.src
                        )
                        movies.add(movie)
                    }

                    CoroutineScope(Dispatchers.IO).launch {
                        movieDao.removeAll()
                        movieDao.save(movies)
                        listener.onSuccess()
                    }
                } else {
                    listener.onFail("Não foi possível completar a operação")
                }
            }

            override fun onFailure(call: Call<MovieBodyResponse>, t: Throwable) {
                listener.onFail(t.message)
            }
        })
    }
}