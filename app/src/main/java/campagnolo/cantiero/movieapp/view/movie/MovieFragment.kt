package campagnolo.cantiero.movieapp.view.movie

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campagnolo.cantiero.movieapp.R
import campagnolo.cantiero.movieapp.databinding.FragmentMovieBinding
import campagnolo.cantiero.movieapp.services.data.entity.Movie
import campagnolo.cantiero.movieapp.utils.MovieConsts
import campagnolo.cantiero.movieapp.view.movie.viewmodel.IMovieClickListener
import campagnolo.cantiero.movieapp.view.movie.viewmodel.IMovieLongClickListener
import campagnolo.cantiero.movieapp.view.movie.viewmodel.MovieAdapter
import campagnolo.cantiero.movieapp.view.movie.viewmodel.MovieViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieFragment : Fragment(), IMovieClickListener, IMovieLongClickListener {
    private lateinit var binding: FragmentMovieBinding
    private val viewModel: MovieViewModel by viewModel()
    lateinit var adapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieBinding.inflate(layoutInflater)
        adapter = MovieAdapter(this, this)

        binding.recyclerMovies.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMovies.adapter = adapter

        setupObservers()
        setupListeners()

        return binding.root
    }

    private fun setupObservers() {
        viewModel.moviesLiveData.observe(viewLifecycleOwner, Observer {
            it.let { movies ->
                adapter.setMovies(movies)
            }
        })

        viewModel.apiLiveData.observe(viewLifecycleOwner, Observer {
            it.let { message ->
                if (!message.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setupListeners() {
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

    private fun edit(movie: Movie) {
        val intent = Intent(requireContext(), MovieDetailsActivity::class.java)
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
        val builder = AlertDialog.Builder(requireContext())

        builder.setMessage("Tem certeza da exclusão?").setCancelable(false)
            .setPositiveButton("Dale!") { _, _ ->
                viewModel.remove(movie)
                viewModel.getAll()
            }.setNegativeButton("Nem") { dialog, _ ->
                dialog.dismiss()

                if (swiped) viewModel.getAll()
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
}