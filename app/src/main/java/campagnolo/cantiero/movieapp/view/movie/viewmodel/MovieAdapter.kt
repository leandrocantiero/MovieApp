package campagnolo.cantiero.movieapp.view.movie.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campagnolo.cantiero.movieapp.model.Movie
import campagnolo.cantiero.movieapp.R
import campagnolo.cantiero.movieapp.utils.MovieConsts
import campagnolo.cantiero.movieapp.databinding.MovieItemBinding
import com.squareup.picasso.Picasso

class MovieAdapter(
    private val onItemClickListener: IMovieClickListener,
    private val onItemLongClickListener: IMovieLongClickListener
) : RecyclerView.Adapter<MovieViewHolder>() {
    private val movies = arrayListOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemBinding =
            MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemBinding, onItemClickListener, onItemLongClickListener)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindView(movies[position])
    }

    override fun getItemCount() = movies.count()

    fun getItemAt(pos: Int) = movies[pos]

    fun setMovies(newData: List<Movie>) {
        movies.clear()
        movies.addAll(newData)
        notifyDataSetChanged()
    }
}

class MovieViewHolder(
    private val itemBinding: MovieItemBinding,
    private val onItemClickListener: IMovieClickListener,
    private val onItemLongClickListener: IMovieLongClickListener
) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bindView(movie: Movie) {
        itemBinding.tvName.text = movie.name
        itemBinding.tvDescription.text = movie.description
        itemBinding.tvRating.text = "${movie.rating}/10"

        if (movie.image.isNotEmpty()) {
            Picasso.with(itemBinding.ivImage.context)
                .load(movie.image)
                .error(R.drawable.ic_launcher_foreground)
                .into(itemBinding.ivImage)
        } else {
            Picasso.with(itemBinding.ivImage.context)
                .load(MovieConsts.BASE_IMAGE)
                .into(itemBinding.ivImage)
        }

        itemView.setOnClickListener() {
            onItemClickListener.onMovieClick(movie)
        }

        itemView.setOnLongClickListener() {
            onItemLongClickListener.onMovieLongClick(movie)
        }
    }
}

interface IMovieClickListener {
    fun onMovieClick(movie: Movie)
}

interface IMovieLongClickListener {
    fun onMovieLongClick(movie: Movie): Boolean
}

