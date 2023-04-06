package campagnolo.cantiero.movieapp.services.api

import campagnolo.cantiero.movieapp.services.api.services.BookService
import campagnolo.cantiero.movieapp.services.api.services.MovieService
import campagnolo.cantiero.movieapp.utils.ApiConsts
import campagnolo.cantiero.movieapp.utils.MovieConsts
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIService {
    private fun initRetrofit(): Retrofit {
        val log = HttpLoggingInterceptor()
        log.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val url = request.url
                val newUrl = url.newBuilder().addQueryParameter("api-key", ApiConsts.API_KEY).build()
                val newRequest = request.newBuilder().url(newUrl).build()

                chain.proceed(newRequest.newBuilder().addHeader("Content-Type", "application/json").build())
            }
            .addInterceptor(log)
            .build()

        return Retrofit.Builder()
            .baseUrl(ApiConsts.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
    }

    val movieService: MovieService = initRetrofit().create(MovieService::class.java)

    val bookService: BookService = initRetrofit().create(BookService::class.java)
}