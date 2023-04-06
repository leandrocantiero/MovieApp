package campagnolo.cantiero.movieapp.services.data.dao

import androidx.room.*
import campagnolo.cantiero.movieapp.services.data.entity.Movie

@Dao
interface MovieDAO {
    @Upsert
    suspend fun save(movie: Movie)

    @Upsert
    suspend fun save(movies: List<Movie>)

    @Query("SELECT * FROM movie ORDER BY name")
    suspend fun getAll(): List<Movie>

    @Query("SELECT * FROM movie WHERE name like :search ORDER BY name")
    suspend fun getByName(search: String?): List<Movie>

    @Delete
    suspend fun remove(movie: Movie)

    @Query("DELETE FROM movie")
    suspend fun removeAll()
}