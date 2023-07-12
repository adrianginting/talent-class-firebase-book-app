package com.adrian.talentclassfirebasebookapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrian.talentclassfirebasebookapp.adapter.BookAdapter
import com.adrian.talentclassfirebasebookapp.data.BookFirebaseRealtimeDBModel
import com.adrian.talentclassfirebasebookapp.databinding.ActivityMainBinding
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dataRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFAB()

        settingFirebaseRealtimeDB()

        readDataFromFirebaseRealtimeDB()

    }

    private fun setFAB() {
        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, AddBookActivity::class.java))
        }
    }

    private fun settingFirebaseRealtimeDB() {
        dataRef = FirebaseDatabase.getInstance().getReference("books")
    }

    private fun readDataFromFirebaseRealtimeDB() {

        val bookList = mutableListOf<BookFirebaseRealtimeDBModel>()

        dataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                bookList.clear()
                for (snapshot in dataSnapshot.children) {
                    val key = snapshot.key.toString()
                    val bookCoverURL = snapshot.child("coverURL").value.toString()
                    val bookTitle = snapshot.child("title").value.toString()
                    val authorName = snapshot.child("author").value.toString()
                    val publicationYear = snapshot.child("year_release").value.toString()
                    val category = snapshot.child("category").value.toString()

                    val book = BookFirebaseRealtimeDBModel(
                        key,
                        bookCoverURL,
                        bookTitle,
                        authorName,
                        publicationYear,
                        category
                    )

                    bookList.add(book)
                }
                showRecyclerListBooks(bookList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Penanganan kesalahan saat membaca data
                Toast.makeText(this@MainActivity, "Gagal membaca data!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showRecyclerListBooks(book: List<BookFirebaseRealtimeDBModel>) {
        val rvBookListAdapter = BookAdapter()

        binding.rvBooks.layoutManager = LinearLayoutManager(this)
        binding.rvBooks.adapter = rvBookListAdapter
        rvBookListAdapter.addedListOfBooks(book)


    }

}