package campagnolo.cantiero.movieapp.model

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import campagnolo.cantiero.movieapp.services.dao.MovieDAO
import campagnolo.cantiero.movieapp.utils.populateDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [Movie::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDAO

    companion object {
        private var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "movies.db"

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            DB_NAME
                        ).addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)

                                Log.d("MoviesDatabase", "populating with data...")
                                GlobalScope.launch(Dispatchers.IO) { populateDB(INSTANCE) }
                            }
                        }).allowMainThreadQueries().build()
                    }
                }
            }

            return INSTANCE!!
        }
    }
}