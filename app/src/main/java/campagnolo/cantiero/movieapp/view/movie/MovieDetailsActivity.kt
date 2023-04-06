package campagnolo.cantiero.movieapp.view.movie

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import campagnolo.cantiero.movieapp.services.data.entity.Movie
import campagnolo.cantiero.movieapp.R
import campagnolo.cantiero.movieapp.utils.MovieConsts
import campagnolo.cantiero.movieapp.view.movie.viewmodel.MovieViewModel
import campagnolo.cantiero.movieapp.databinding.ActivityMovieDetailsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailsBinding
    private val viewModel: MovieViewModel by viewModel()

    private var movieId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        loadData()
        setupToolbar()
    }

    private fun setupListeners() {
        binding.edName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length in 0..3) {
                    binding.edName.error = "Este campo é obrigatório"
                }
            }
        })

        binding.edDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length in 0..3) {
                    binding.edDescription.error = "Este campo é obrigatório"
                }
            }
        })

        binding.sbRating.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val value = (progress / 10.0).toFloat()
                binding.tvRatingProgress.text = value.toString()
//                val `val`: Int =
//                    progress * (seekBar.width - 2 * seekBar.thumbOffset) / seekBar.max
//                binding.tvRatingProgress.x = seekBar.x + `val` + seekBar.thumbOffset / 2
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
    }

    private fun loadData() {
        val extras = intent.extras
        if (extras != null) {
            movieId = extras.getInt(MovieConsts.ID)
            binding.edName.setText(extras.getString(MovieConsts.NAME))
            binding.edDescription.setText(extras.getString(MovieConsts.DESCRIPTION))
            binding.edImage.setText(extras.getString(MovieConsts.IMAGE))
            val rating = extras.getDouble(MovieConsts.RATING) * 10.0
            binding.sbRating.progress = rating.toInt()
        }
    }

    private fun setupToolbar() {
        val toolbar: ActionBar? = supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(true)

        if (movieId == 0) {
            toolbar?.title = "Adicionar filme"
        } else {
            toolbar?.title = "Alterar filme"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.action_save -> {
                save()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun save() {
        val movie = Movie(
            id = movieId,
            name = binding.edName.text.toString(),
            description = binding.edDescription.text.toString(),
            rating = binding.sbRating.progress.toDouble() / 10.0,
            image = binding.edImage.text.toString()
        )

        if (movie.name.isEmpty() || movie.description.isEmpty())
            return Toast.makeText(this, "Preencha nome e descrição!", Toast.LENGTH_SHORT).show()

        viewModel.save(movie)
        finish()
    }
}