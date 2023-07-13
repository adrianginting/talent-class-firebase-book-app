package com.adrian.talentclassfirebasebookapp

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.adrian.talentclassfirebasebookapp.data.BookFirebaseRealtimeDBModel
import com.adrian.talentclassfirebasebookapp.databinding.ActivityEditBookBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBookBinding
    private lateinit var dataRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBookBinding.inflate(layoutInflater)
        supportActionBar?.setTitle("Edit Buku")
        setContentView(binding.root)
        dataRef = FirebaseDatabase.getInstance().getReference("books")

        setData()
        setButton()
    }
    private fun setData() {
        if (intent.extras != null) {
            val bookTitle = intent.getStringExtra("bookTitle")!!
            val authorName = intent.getStringExtra("authorName")!!
            val publicationYear = intent.getStringExtra("publicationYear")!!
            val category = intent.getStringExtra("category")!!
            val bookCoverURL = intent.getStringExtra("bookCoverURL")!!

            binding.idEdtTitle.text = Editable.Factory.getInstance().newEditable(bookTitle)
            binding.idEdtAuthor.text = Editable.Factory.getInstance().newEditable(authorName)
            binding.idEdtYearRelease.text = Editable.Factory.getInstance().newEditable(publicationYear)
            binding.idEdtLink.text = Editable.Factory.getInstance().newEditable(bookCoverURL)

            val listCategory = arrayOf("Komik",
                "Novel",
                "Biografi",
                "Majalah",
                "Ensiklopedia",
                "Naskah")

            // Membuat adapter untuk dropdown
            val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, listCategory)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            // Menetapkan adapter ke spinner
            binding.idSpinnerCategory.adapter = adapter

            val selectedIndex = listCategory.indexOf(category)
            binding.idSpinnerCategory.setSelection(selectedIndex)

        }
    }

    private fun setButton() {
        binding.apply {
            idBtnSaveData.setOnClickListener {
                editData()
            }
            idBtnDeleteData.setOnClickListener {
                showDeleteConfirmationDialog()
            }
            idBtnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun editData() {
        val previousBookTitle = intent.getStringExtra("bookTitle")!!
        val key = intent.getStringExtra("key")!!
        val bookTitle = binding.idEdtTitle.text.toString()
        val authorName = binding.idEdtAuthor.text.toString()
        val publicationYear = binding.idEdtYearRelease.text.toString()
        val category = binding.idSpinnerCategory.selectedItem.toString()
        val bookCoverURL = binding.idEdtLink.text.toString()

        if (bookTitle.isEmpty()) {
            binding.idEdtTitle.error = "Judul Buku tidak boleh kosong"
        }

        if (authorName.isEmpty()) {
            binding.idEdtAuthor.error = "Nama Penulis tidak boleh kosong"
        }

        if (publicationYear.isEmpty()) {
            binding.idEdtYearRelease.error = "Tahun terbit tidak boleh kosong"
        }

        if (bookCoverURL.isEmpty()) {
            binding.idEdtLink.error = "URL Cover Buku tidak boleh kosong"
        }

        if (bookTitle.isNotEmpty() && authorName.isNotEmpty() && publicationYear.isNotEmpty() && category.isNotEmpty() && bookCoverURL.isNotEmpty()) {
            val book = BookFirebaseRealtimeDBModel(
                key,
                bookCoverURL,
                bookTitle,
                authorName,
                publicationYear,
                category
            )

            dataRef.child(previousBookTitle).removeValue()

            dataRef.child(key).setValue(book).addOnSuccessListener {
                Toast.makeText(this@EditBookActivity, "Data berhasil diubah!", Toast.LENGTH_SHORT).show()
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
                .addOnCanceledListener {
                    Toast.makeText(this@EditBookActivity, "Batal diubah!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this@EditBookActivity, "Data Gagal diubah!", Toast.LENGTH_SHORT).show()
                }
        }

    }


    private fun deleteDataBookFirebase() {
        val key = intent.getStringExtra("key")!!
        val bookRef = dataRef.child(key)

        bookRef.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this@EditBookActivity, "Data berhasil dihapus!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@EditBookActivity, "Gagal menghapus data!", Toast.LENGTH_SHORT).show()
            }
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi")
            .setMessage("Anda yakin ingin menghapus data ini?")
            .setPositiveButton("Ya") { _, _ ->
                deleteDataBookFirebase()
            }
            .setNegativeButton("Tidak", null)
            .show()
    }
}