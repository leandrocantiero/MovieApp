package campagnolo.cantiero.movieapp.view.book

import android.content.Intent
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
import campagnolo.cantiero.movieapp.databinding.FragmentBookBinding
import campagnolo.cantiero.movieapp.services.data.entity.Book
import campagnolo.cantiero.movieapp.utils.BookConsts
import campagnolo.cantiero.movieapp.view.book.viewmodel.BookAdapter
import campagnolo.cantiero.movieapp.view.book.viewmodel.BookViewModel
import campagnolo.cantiero.movieapp.view.book.viewmodel.IBookClickListener
import campagnolo.cantiero.movieapp.view.book.viewmodel.IBookLongClickListener
import campagnolo.cantiero.movieapp.view.movie.MovieDetailsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookFragment : Fragment(), IBookLongClickListener, IBookClickListener {
    private lateinit var binding: FragmentBookBinding
    private val viewModel: BookViewModel by viewModel()
    lateinit var adapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookBinding.inflate(layoutInflater)
        adapter = BookAdapter(this, this)

        binding.recyclerBooks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerBooks.adapter = adapter

        setupObservers()
        setupListeners()

        return binding.root
    }

    private fun setupObservers() {
        viewModel.booksLiveData.observe(viewLifecycleOwner, Observer {
            it.let { books ->
                adapter.setBooks(books)
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
                    viewModel.getByTitle(s.toString())
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
                    val book = adapter.getItemAt(pos)

                    remove(book, true)
                }
            }
        }).attachToRecyclerView(binding.recyclerBooks)
    }

    private fun edit(book: Book) {
        val intent = Intent(requireContext(), MovieDetailsActivity::class.java)
        val bundle = Bundle()

        bundle.putInt(BookConsts.ID, book.id)
        bundle.putString(BookConsts.TITLE, book.title)
        bundle.putString(BookConsts.DESCRIPTION, book.description)
        bundle.putString(BookConsts.AUTHOR, book.author)

        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun remove(book: Book, swiped: Boolean = false) {
        val builder = AlertDialog.Builder(requireContext())

        builder.setMessage("Tem certeza da exclusão?").setCancelable(false)
            .setPositiveButton("Dale!") { _, _ ->
                viewModel.remove(book)
                viewModel.getAll()
            }.setNegativeButton("Nem") { dialog, _ ->
                dialog.dismiss()

                if (swiped) viewModel.getAll()
            }
        val alert = builder.create()
        alert.show()
    }

    override fun onBookClick(book: Book) {
        edit(book)
    }

    override fun onBookLongClick(book: Book): Boolean {
        remove(book)
        return true
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAll()
    }
}