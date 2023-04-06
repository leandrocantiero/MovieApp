package campagnolo.cantiero.movieapp.view.book

import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import campagnolo.cantiero.movieapp.R
import campagnolo.cantiero.movieapp.databinding.ActivityBookDetailsBinding
import campagnolo.cantiero.movieapp.services.data.entity.Book
import campagnolo.cantiero.movieapp.utils.BookConsts
import campagnolo.cantiero.movieapp.utils.browseTo
import campagnolo.cantiero.movieapp.view.book.viewmodel.BookViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookDetailsBinding
    private val viewModel: BookViewModel by viewModel()

    private var bookId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        loadData()
        setupToolbar()
    }

    private fun setupListeners() {
        binding.btAmazonUrl.setOnClickListener {
            if (binding.edAmazonUrl.text.isEmpty()) {
                Toast.makeText(this, "Preencha o campo URL da Amazon para continuar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            browseTo(applicationContext, binding.edAmazonUrl.text.toString())
        }

        binding.edTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length in 0..3) {
                    binding.edTitle.error = "Este campo é obrigatório"
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
    }

    private fun loadData() {
        val extras = intent.extras
        if (extras != null) {
            bookId = extras.getInt(BookConsts.ID)
            binding.edTitle.setText(extras.getString(BookConsts.TITLE))
            binding.edDescription.setText(extras.getString(BookConsts.DESCRIPTION))
            binding.edAuthor.setText(extras.getString(BookConsts.AUTHOR))
        }
    }

    private fun setupToolbar() {
        val toolbar: ActionBar? = supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(true)

        if (bookId == 0) {
            toolbar?.title = "Adicionar livro"
        } else {
            toolbar?.title = "Alterar livro"

            binding.btAmazonUrl.visibility = View.VISIBLE
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
        val book = Book(
            id = bookId,
            title = binding.edTitle.text.toString(),
            description = binding.edDescription.text.toString(),
            author = binding.edAuthor.text.toString(),
            amazonUrl = binding.edAmazonUrl.text.toString()
        )

        if (book.title.isEmpty() || book.description.isEmpty())
            return Toast.makeText(this, "Preencha título e descrição!", Toast.LENGTH_SHORT).show()

        viewModel.save(book)
        finish()
    }
}