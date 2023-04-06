package campagnolo.cantiero.movieapp.services.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import campagnolo.cantiero.movieapp.services.data.dao.BookDAO
import campagnolo.cantiero.movieapp.services.data.dao.MovieDAO
import campagnolo.cantiero.movieapp.services.data.entity.Book
import campagnolo.cantiero.movieapp.services.data.entity.Movie
import campagnolo.cantiero.movieapp.utils.populateDB
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [Movie::class, Book::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDAO

    abstract fun bookDao(): BookDAO

    companion object {
        private var database: AppDatabase? = null
        private var inMemoryDatabase: AppDatabase? = null

        private const val DB_NAME = "movies.db"

        @OptIn(DelicateCoroutinesApi::class)
        fun getDatabase(context: Context): AppDatabase {
            if (database == null) {
                synchronized(AppDatabase::class.java) {
                    if (database == null) {
                        database = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            DB_NAME
                        ).build()

//                            .addCallback(object : Callback() {
//                            override fun onCreate(db: SupportSQLiteDatabase) {
//                                super.onCreate(db)
//
//                                Log.d("MoviesDatabase", "populating with data...")
//                                GlobalScope.launch(Dispatchers.IO) { populateDB(database) }
//                            }
//                        }).allowMainThreadQueries().build()
                    }
                }
            }

            return database!!
        }

        fun getInMemoryDatabase(context: Context): AppDatabase {
            if (inMemoryDatabase == null) {
                synchronized(AppDatabase::class.java) {
                    if (inMemoryDatabase == null) {
                        inMemoryDatabase = Room.inMemoryDatabaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java
                        ).build()
                    }
                }
            }

            return inMemoryDatabase!!
        }
    }
}