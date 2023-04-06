package campagnolo.cantiero.movieapp.di

import campagnolo.cantiero.movieapp.services.data.repository.BookRepository
import campagnolo.cantiero.movieapp.view.book.viewmodel.BookViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val bookModule = module {
    factory { BookRepository(androidContext()) }

    viewModel {
        BookViewModel(repository = get())
    }
}