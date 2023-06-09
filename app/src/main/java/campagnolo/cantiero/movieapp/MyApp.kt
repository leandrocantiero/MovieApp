package campagnolo.cantiero.movieapp

import android.app.Application
import campagnolo.cantiero.movieapp.di.bookModule
import campagnolo.cantiero.movieapp.di.movieModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyApp)

            modules(movieModule, bookModule)
        }
    }
}