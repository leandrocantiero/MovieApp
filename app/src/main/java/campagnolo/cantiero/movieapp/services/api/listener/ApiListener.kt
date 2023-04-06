package campagnolo.cantiero.movieapp.services.api.listener

interface ApiListener {
    fun onFail(message: String?)

    fun onSuccess()
}