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
import com.eno.ensberita.Models.Berita
import com.eno.ensberita.databinding.ViewholderBeritaBinding

class PelangganAdapter (private val items:ArrayList<Berita>): RecyclerView.Adapter<PelangganAdapter.Viewholder>() {
    private var context: Context?=null

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
                val intent= Intent(context, BeritaDetailActivity::class.java)
                intent.putExtra("object",berita)
                context!!.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PelangganAdapter.Viewholder {
        context=parent.context
        val binding=ViewholderBeritaBinding.inflate(LayoutInflater.from(context),parent,false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: PelangganAdapter.Viewholder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int =items.size
}