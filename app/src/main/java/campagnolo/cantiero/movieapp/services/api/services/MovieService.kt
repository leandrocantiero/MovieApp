package campagnolo.cantiero.movieapp.services.api.services

import campagnolo.cantiero.movieapp.model.api.MovieBodyResponse
import campagnolo.cantiero.movieapp.utils.MovieConsts
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("all.json")
    fun getMovies(
        @Query("api-key") list: String = MovieConsts.API_KEY
    ): Call<MovieBodyResponse>
}