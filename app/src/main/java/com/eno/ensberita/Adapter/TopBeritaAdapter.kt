package com.eno.ensberita.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.eno.ensberita.Activity.BeritaDetailActivity
import com.eno.ensberita.Models.TopBerita
import com.eno.ensberita.databinding.ViewholderBeritaBinding

class TopBeritaAdapter(private val items: ArrayList<TopBerita>) :
    RecyclerView.Adapter<TopBeritaAdapter.Viewholder>() {

    inner class Viewholder(private val binding: ViewholderBeritaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(topBerita: TopBerita) {
            binding.nameTxt.text = topBerita.Title
            val requestOptions = RequestOptions()
                .transform(CenterCrop(), RoundedCorners(30))

            // Pastikan 'context' aman diakses dari 'binding.root.context'
            val safeContext = binding.root.context

            Glide.with(safeContext)
                .load(topBerita.Poster)
                .apply(requestOptions)
                .into(binding.pic)

            binding.root.setOnClickListener {
                val intent = Intent(safeContext, BeritaDetailActivity::class.java)
                intent.putExtra("object", topBerita)
                safeContext.startActivity(intent) // Menggunakan safeContext yang pasti tidak null
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val binding = ViewholderBeritaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
