package campagnolo.cantiero.movieapp.view.book.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campagnolo.cantiero.movieapp.databinding.BookItemBinding
import campagnolo.cantiero.movieapp.services.data.entity.Book

class BookAdapter(
    private val onItemClickListener: IBookClickListener,
    private val onItemLongClickListener: IBookLongClickListener
) : RecyclerView.Adapter<BookViewHolder>() {
    private val books = arrayListOf<Book>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemBinding =
            BookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(itemBinding, onItemClickListener, onItemLongClickListener)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bindView(books[position])
    }

    override fun getItemCount() = books.size

    fun getItemAt(pos: Int) = books[pos]

    fun setBooks(newData: List<Book>) {
        books.clear()
        books.addAll(newData)
        notifyDataSetChanged()
    }
}

class BookViewHolder(
    private val itemBinding: BookItemBinding,
    private val onItemClickListener: IBookClickListener,
    private val onItemLongClickListener: IBookLongClickListener
) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bindView(book: Book) {
        itemBinding.tvTitle.text = book.title
        itemBinding.tvDescription.text = book.description
        itemBinding.tvAuthor.text = book.author

        itemView.setOnClickListener() {
            onItemClickListener.onBookClick(book)
        }

        itemView.setOnLongClickListener() {
            onItemLongClickListener.onBookLongClick(book)
        }
    }
}

interface IBookClickListener {
    fun onBookClick(book: Book)
}

interface IBookLongClickListener {
    fun onBookLongClick(book: Book): Boolean
}