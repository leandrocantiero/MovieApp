package campagnolo.cantiero.movieapp.view.book.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campagnolo.cantiero.movieapp.services.data.entity.Book
import campagnolo.cantiero.movieapp.services.api.listener.ApiListener
import campagnolo.cantiero.movieapp.services.data.repository.BookRepository
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository) : ViewModel() {
    private val mBooksLiveData = MutableLiveData<List<Book>>()
    var booksLiveData: LiveData<List<Book>> = mBooksLiveData

    private var mApiLiveData: MutableLiveData<String?> = MutableLiveData()
    var apiLiveData: LiveData<String?> = mApiLiveData

    fun save(book: Book) {
        viewModelScope.launch {
            repository.save(book)
        }
    }

    fun getAll() {
        viewModelScope.launch {
            mBooksLiveData.value = repository.getAll()
        }
    }

    fun getByTitle(title: String) {
        viewModelScope.launch {
            mBooksLiveData.value = repository.getByTitle(title)
        }
    }

    fun remove(book: Book) {
        viewModelScope.launch {
            repository.remove(book)
        }
    }

    fun getAllFromApi() {
        repository.getAllFromApi(object : ApiListener {
            override fun onFail(message: String?) {
                mApiLiveData.value = message
            }

            override fun onSuccess() {
                getAll()
            }

        })
    }
}