package com.adrian.talentclassfirebasebookapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adrian.talentclassfirebasebookapp.data.BookFirebaseRealtimeDBModel
import com.adrian.talentclassfirebasebookapp.databinding.ActivityAddBookBinding
import com.google.firebase.database.FirebaseDatabase

class AddBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.idBtnSendData.setOnClickListener {
            val imageCoverUrl = binding.idEdtLink.text.toString()
            val title = binding.idEdtTitle.text.toString()
            val author = binding.idEdtAuthor.text.toString()
            val release = binding.idEdtYearRelease.text.toString()
            val category = binding.idEdtCategory.text.toString()

            val userRef = FirebaseDatabase.getInstance().getReference("books")
            val newUserRef = userRef.push()
            val user = BookFirebaseRealtimeDBModel("key",imageCoverUrl, title, author, release, category)
            newUserRef.setValue(user)

            finish()
        }
    }
}