package campagnolo.cantiero.movieapp.model.api

import com.google.gson.annotations.SerializedName

class MovieResultResponse(
    @SerializedName("display_title")
    val displayTitle: String,

    @SerializedName("summary_short")
    val summaryShort: String,

    val multimedia: MovieImageResponse,
)