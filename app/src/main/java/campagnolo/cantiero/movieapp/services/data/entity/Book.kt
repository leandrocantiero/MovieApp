package campagnolo.cantiero.movieapp.services.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    val author: String,
    val amazonUrl: String
)