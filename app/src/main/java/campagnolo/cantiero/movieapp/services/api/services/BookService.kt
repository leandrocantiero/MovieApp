package campagnolo.cantiero.movieapp.services.api.services

import campagnolo.cantiero.movieapp.services.api.model.BookBodyResponse
import campagnolo.cantiero.movieapp.utils.ApiConsts
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    @GET("books/v3/lists.json")
    fun getBooks(
        @Query("api-key") apiKey: String = ApiConsts.API_KEY,
        @Query("list") list: String = "hardcover-fiction"
    ): Call<BookBodyResponse>
}