package campagnolo.cantiero.movieapp.view

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import campagnolo.cantiero.movieapp.R
import campagnolo.cantiero.movieapp.databinding.ActivityMainBinding
import campagnolo.cantiero.movieapp.view.book.BookDetailsActivity
import campagnolo.cantiero.movieapp.view.book.viewmodel.BookViewModel
import campagnolo.cantiero.movieapp.view.movie.viewmodel.MovieViewModel
import campagnolo.cantiero.movieapp.view.movie.MovieDetailsActivity
import com.google.android.material.navigation.NavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val movieViewModel: MovieViewModel by viewModel()
    private val bookViewModel: BookViewModel by viewModel()

    private var isMenuOpen: Boolean = false
    private val interpolator = OvershootInterpolator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupNavView()
        setupListeners()

        initFabs()
    }

    private fun initFabs() {
        binding.fabAddMovie.alpha = 0f
        binding.fabAddBook.alpha = 0f

        binding.fabAddMovie.translationY = 100f
        binding.fabAddBook.translationY = 100f
    }

    private fun setupListeners() {
        binding.fabAdd.setOnClickListener {
            openOrCloseFabs()
        }

        binding.fabAddMovie.setOnClickListener {
            addMovie()
            openOrCloseFabs()
        }

        binding.fabAddBook.setOnClickListener {
            addBook()
            openOrCloseFabs()
        }
    }

    private fun openOrCloseFabs() {
        if (!isMenuOpen) {
            binding.fabAdd.animate().setInterpolator(interpolator).rotation(45f).setDuration(300)
                .start()

            binding.fabAddMovie.animate()
                .translationY(0f)
                .alpha(1f)
                .setInterpolator(interpolator)
                .setDuration(300)
                .start()

            binding.fabAddBook.animate()
                .translationY(0f)
                .alpha(1f)
                .setInterpolator(interpolator)
                .setDuration(300)
                .start()
        } else {
            binding.fabAdd.animate().setInterpolator(interpolator).rotation(0f).setDuration(300)
                .start()

            binding.fabAddMovie.animate()
                .translationY(100f)
                .alpha(0f)
                .setInterpolator(interpolator)
                .setDuration(300)
                .start()

            binding.fabAddBook.animate()
                .translationY(100f)
                .alpha(0f)
                .setInterpolator(interpolator)
                .setDuration(300)
                .start()
        }

        isMenuOpen = !isMenuOpen
    }

    private fun setupNavView() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_view)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_movie, R.id.nav_book), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun addMovie() {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        startActivity(intent)
    }

    private fun addBook() {
        val intent = Intent(this, BookDetailsActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_request_movies -> {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setMessage("Isso excluirá todos os filmes cadastrados e adicionará novos, deseja continuar?")
                    .setCancelable(false)
                    .setPositiveButton("Dale!") { _, _ ->
                        movieViewModel.getAllFromApi()
                    }.setNegativeButton("Nem") { dialog, _ ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()

                true
            }

            R.id.action_request_books -> {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setMessage("Isso excluirá todos os livros cadastrados e adicionará novos, deseja continuar?")
                    .setCancelable(false)
                    .setPositiveButton("Dale!") { _, _ ->
                        bookViewModel.getAllFromApi()
                    }.setNegativeButton("Nem") { dialog, _ ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()

                true
            }

            R.id.action_about -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_view)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        when {
            binding.drawerLayout.isDrawerOpen(GravityCompat.START) -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            else -> {
                super.onBackPressed()
            }
        }
    }
}