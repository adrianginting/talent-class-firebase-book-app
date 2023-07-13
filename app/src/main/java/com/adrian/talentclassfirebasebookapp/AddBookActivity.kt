package com.adrian.talentclassfirebasebookapp

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.adrian.talentclassfirebasebookapp.data.BookFirebaseRealtimeDBModel
import com.adrian.talentclassfirebasebookapp.databinding.ActivityAddBookBinding
import com.google.firebase.database.FirebaseDatabase

class AddBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dropdownCategory()

        binding.idBtnSendData.setOnClickListener {
            val imageCoverUrl = binding.idEdtLink.text.toString()
            val title = binding.idEdtTitle.text.toString()
            val author = binding.idEdtAuthor.text.toString()
            val release = binding.idEdtYearRelease.text.toString()
            val category = binding.idSpinnerCategory.selectedItem.toString()

            if (title.isEmpty()) {
                binding.idEdtTitle.error = "Judul Buku tidak boleh kosong"
            }

            if (author.isEmpty()) {
                binding.idEdtAuthor.error = "Nama Penulis tidak boleh kosong"
            }

            if (release.isEmpty()) {
                binding.idEdtYearRelease.error = "Tahun terbit tidak boleh kosong"
            }

            if (imageCoverUrl.isEmpty()) {
                binding.idEdtLink.error = "URL Cover Buku tidak boleh kosong"
            }

            if (title.isNotEmpty() && author.isNotEmpty() && release.isNotEmpty() && imageCoverUrl.isNotEmpty()){
                val userRef = FirebaseDatabase.getInstance().getReference("books")
                val newUserRef = userRef.push()
                val user = BookFirebaseRealtimeDBModel("key",imageCoverUrl, title, author, release, category)
                newUserRef.setValue(user)

                finish()
            }
        }

        binding.idBtnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun dropdownCategory() {
        // Daftar item dropdown
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

        // Menangani peristiwa saat item dipilih
        binding.idSpinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedLanguage = parent.getItemAtPosition(position).toString()

                if (position != 0) {
                    Toast.makeText(this@AddBookActivity, "Anda memilih: $selectedLanguage", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Tidak ada item yang dipilih
            }
        }
    }
}