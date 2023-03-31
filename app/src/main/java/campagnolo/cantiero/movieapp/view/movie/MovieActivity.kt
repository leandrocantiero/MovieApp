package campagnolo.cantiero.movieapp.view.movie

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campagnolo.cantiero.movieapp.model.Movie
import campagnolo.cantiero.movieapp.R
import campagnolo.cantiero.movieapp.utils.MovieConsts
import campagnolo.cantiero.movieapp.view.movie.viewmodel.IMovieClickListener
import campagnolo.cantiero.movieapp.view.movie.viewmodel.IMovieLongClickListener
import campagnolo.cantiero.movieapp.view.movie.viewmodel.MovieAdapter
import campagnolo.cantiero.movieapp.view.movie.viewmodel.MovieViewModel
import campagnolo.cantiero.movieapp.databinding.ActivityMovieBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieActivity : AppCompatActivity(), IMovieClickListener, IMovieLongClickListener {

    private lateinit var binding: ActivityMovieBinding
    private val viewModel: MovieViewModel by viewModel()
    lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        adapter = MovieAdapter(this, this)

        setupObservers()
        setupListeners()

        binding.recyclerMovies.layoutManager = LinearLayoutManager(this)
        binding.recyclerMovies.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.moviesLiveData.observe(this, Observer {
            it.let { movies ->
                adapter.setMovies(movies)
            }
        })

        viewModel.apiLiveData.observe(this, Observer {
            it.let { message ->
                if (!message.isNullOrEmpty()) {
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setupListeners() {
        binding.fab.setOnClickListener { add() }

        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.isEmpty()) {
                    viewModel.getAll()
                } else {
                    viewModel.getByName(s.toString())
                }
            }

        })

        // swipe delete
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (viewHolder.absoluteAdapterPosition > -1) {
                    val pos = viewHolder.absoluteAdapterPosition
                    val movie = adapter.getItemAt(pos)

                    remove(movie, true)
                }
            }
        }).attachToRecyclerView(binding.recyclerMovies)
    }

    private fun add() {
        val intent = Intent(this@MovieActivity, MovieDetailsActivity::class.java)
        startActivity(intent)
    }

    private fun edit(movie: Movie) {
        val intent = Intent(this@MovieActivity, MovieDetailsActivity::class.java)
        val bundle = Bundle()

        bundle.putInt(MovieConsts.ID, movie.id)
        bundle.putString(MovieConsts.NAME, movie.name)
        bundle.putString(MovieConsts.DESCRIPTION, movie.description)
        bundle.putString(MovieConsts.IMAGE, movie.image)
        bundle.putDouble(MovieConsts.RATING, movie.rating)

        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun remove(movie: Movie, swiped: Boolean = false) {
        val builder = AlertDialog.Builder(this@MovieActivity)
        builder.setMessage("Tem certeza da exclusão?").setCancelable(false)
            .setPositiveButton("Dale!") { _, _ ->
                viewModel.remove(movie)
                viewModel.getAll()
            }.setNegativeButton("Nem") { dialog, _ ->
                dialog.dismiss()

                if (swiped)
                    viewModel.getAll()
            }
        val alert = builder.create()
        alert.show()
    }

    override fun onMovieClick(movie: Movie) {
        edit(movie)
    }

    override fun onMovieLongClick(movie: Movie): Boolean {
        remove(movie)
        return true
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAll()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_request_movies -> {
                val builder = AlertDialog.Builder(this@MovieActivity)
                builder.setMessage("Isso excluirá todos os filmes cadastrados e adicionará novos, deseja continuar?").setCancelable(false)
                    .setPositiveButton("Dale!") { _, _ ->
                        viewModel.getMoviesAPI()
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
}