package campagnolo.cantiero.movieapp.services.api.model

import com.google.gson.annotations.SerializedName

class BookResultResponse(
    @SerializedName("book_details")
    val bookDetails: List<BookDetailsResponse>,
    @SerializedName("amazon_product_url")
    val amazonUrl: String
)