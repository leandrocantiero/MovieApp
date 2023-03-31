package campagnolo.cantiero.movieapp.services.repository

import android.content.Context
import campagnolo.cantiero.movieapp.model.AppDatabase
import campagnolo.cantiero.movieapp.model.api.MovieBodyResponse
import campagnolo.cantiero.movieapp.model.Movie
import campagnolo.cantiero.movieapp.services.api.APIService
import campagnolo.cantiero.movieapp.services.api.listener.MovieListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRepository(val context: Context) : IMovieRepository {
    private val movieDAO = AppDatabase.getDatabase(context).movieDao()

    override suspend fun save(movie: Movie) {
        return movieDAO.save(movie)
    }

    override suspend fun save(movies: List<Movie>) {
        return movieDAO.save(movies)
    }

    override suspend fun getAll(): List<Movie> {
        return movieDAO.getAll()
    }

    override suspend fun getByName(name: String): List<Movie> {
        val search = "%${name}%"
        return movieDAO.getByName(search)
    }

    override suspend fun remove(movie: Movie) {
        return movieDAO.remove(movie)
    }

    override suspend fun getAllFromApi(listener: MovieListener) {
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

                    movieDAO.removeAll()
                    movieDAO.save(movies)
                    listener.onSuccess()
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