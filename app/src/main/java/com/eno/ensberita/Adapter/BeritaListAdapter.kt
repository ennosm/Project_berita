package com.eno.ensberita.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.eno.ensberita.Activity.BeritaDetailActivity
import com.eno.ensberita.Activity.IntroActivity
import com.eno.ensberita.Models.Berita
import com.eno.ensberita.databinding.ViewholderBeritaBinding

class BeritaListAdapter(private val items:ArrayList<Berita>): RecyclerView.Adapter<BeritaListAdapter.Viewholder>() {
    private var context:Context?=null

    inner class Viewholder(private val binding: ViewholderBeritaBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(berita: Berita) {
            binding.nameTxt.text = berita.title
            val requestOptions = RequestOptions()
                .transform(CenterCrop(), RoundedCorners(30))

            // Pastikan 'context' tidak null dengan cara aman
            val safeContext = binding.root.context

            Glide.with(safeContext)
                .load(berita.poster)
                .apply(requestOptions)
                .into(binding.pic)

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, BeritaDetailActivity::class.java)
                intent.putExtra("object", berita)
                binding.root.context.startActivity(intent)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeritaListAdapter.Viewholder {
        context=parent.context
        val binding=ViewholderBeritaBinding.inflate(LayoutInflater.from(context),parent,false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: BeritaListAdapter.Viewholder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int =items.size

    // Fungsi untuk memperbarui data adapter
    fun updateData(newItems: ArrayList<Berita>) {
        items.clear()              // Menghapus data lama
        items.addAll(newItems)     // Menambahkan data baru
        notifyDataSetChanged()     // Memberi tahu RecyclerView untuk memperbarui tampilan
    }
}