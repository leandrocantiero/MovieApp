package campagnolo.cantiero.movieapp.services.dao

import androidx.room.*
import campagnolo.cantiero.movieapp.model.Movie

@Dao
interface MovieDAO {
    @Upsert
    fun save(movie: Movie)

    @Upsert
    fun save(movies: List<Movie>)

    @Query("SELECT * FROM movie ORDER BY name")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM movie WHERE name like :search ORDER BY name")
    fun getByName(search: String?): List<Movie>

    @Delete
    fun remove(movie: Movie)

    @Query("DELETE FROM movie")
    fun removeAll()
}