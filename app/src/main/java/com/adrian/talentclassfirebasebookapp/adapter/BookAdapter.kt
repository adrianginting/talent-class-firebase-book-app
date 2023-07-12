package com.adrian.talentclassfirebasebookapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adrian.talentclassfirebasebookapp.EditBookActivity
import com.adrian.talentclassfirebasebookapp.data.BookFirebaseRealtimeDBModel
import com.adrian.talentclassfirebasebookapp.databinding.ItemRowBookBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class BookAdapter : RecyclerView.Adapter<BookAdapter.BookListViewHolder>() {

    private var listOfBook = ArrayList<BookFirebaseRealtimeDBModel>()

    fun addedListOfBooks(list: List<BookFirebaseRealtimeDBModel>) {
        this.listOfBook.clear()
        this.listOfBook.addAll(list)
        notifyDataSetChanged()
    }


    inner class BookListViewHolder(private val binding: ItemRowBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val itemNow = listOfBook[position]
            binding.tvItemTitle.text = itemNow.title
            binding.tvItemAuthor.text = itemNow.author
            binding.tvItemRelease.text = itemNow.year_release
            binding.tvItemCategory.text = itemNow.category

            Glide
                .with(itemView.context)
                .load(itemNow.coverURL)
                .transform(CenterInside(), RoundedCorners(12))
                .into(binding.imgItemPhoto)

            binding.cardView.setOnClickListener {
                val intent = Intent(itemView.context, EditBookActivity::class.java)
                intent.putExtra("key", itemNow.key)
                intent.putExtra("bookCoverURL", itemNow.coverURL)
                intent.putExtra("bookTitle", itemNow.title)
                intent.putExtra("authorName", itemNow.author)
                intent.putExtra("publicationYear", itemNow.year_release)
                intent.putExtra("category", itemNow.category)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookAdapter.BookListViewHolder {
        return BookListViewHolder(
            ItemRowBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BookAdapter.BookListViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return listOfBook.size
    }
}
