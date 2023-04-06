package campagnolo.cantiero.movieapp.di

import campagnolo.cantiero.movieapp.services.data.repository.MovieRepository
import campagnolo.cantiero.movieapp.view.movie.viewmodel.MovieViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val movieModule = module {
    factory { MovieRepository(context = androidContext()) }

    viewModel {
        MovieViewModel(repository = get())
    }
}