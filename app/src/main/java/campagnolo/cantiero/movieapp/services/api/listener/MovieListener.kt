package campagnolo.cantiero.movieapp.services.api.listener

interface MovieListener {
    fun onFail(message: String?)

    fun onSuccess()
}