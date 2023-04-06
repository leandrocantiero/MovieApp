package campagnolo.cantiero.movieapp.services.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String,
    val rating: Double,
    val image: String
) {
    constructor(name: String, description: String) : this(0, name, description, 0.0, "")
    constructor(name: String, description: String, rating: Double, image: String) : this(0, name, description, rating, image)
}