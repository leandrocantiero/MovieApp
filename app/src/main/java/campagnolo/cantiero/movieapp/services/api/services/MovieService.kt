package campagnolo.cantiero.movieapp.services.api.services

import campagnolo.cantiero.movieapp.services.api.model.MovieBodyResponse
import campagnolo.cantiero.movieapp.utils.ApiConsts
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("movies/v2/reviews/all.json")
    fun getMovies(
        @Query("api-key") list: String = ApiConsts.API_KEY
    ): Call<MovieBodyResponse>
}