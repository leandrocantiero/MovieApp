package campagnolo.cantiero.movieapp.view.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campagnolo.cantiero.movieapp.model.Movie
import campagnolo.cantiero.movieapp.services.api.listener.MovieListener
import campagnolo.cantiero.movieapp.services.repository.IMovieRepository
import campagnolo.cantiero.movieapp.services.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {
    private var mMoviesLiveData: MutableLiveData<List<Movie>> = MutableLiveData()
    var moviesLiveData: LiveData<List<Movie>> = mMoviesLiveData

    private var mApiLiveData: MutableLiveData<String?> = MutableLiveData()
    var apiLiveData: LiveData<String?> = mApiLiveData

    fun getAll() {
        viewModelScope.launch {
            mMoviesLiveData.value = repository.getAll()
        }
    }

    fun getByName(name: String) {
        viewModelScope.launch {
            mMoviesLiveData.value = repository.getByName(name)
        }
    }

    fun save(movie: Movie) {
        viewModelScope.launch {
            repository.save(movie)
        }
    }

    fun getMoviesAPI() {
        viewModelScope.launch {
            repository.getAllFromApi(object : MovieListener {
                override fun onFail(message: String?) {
                    mApiLiveData.value = message
                }

                override fun onSuccess() {
                    getAll()
                }

            })
        }
    }

    fun remove(movie: Movie) {
        viewModelScope.launch {
            repository.remove(movie)
        }
    }
}