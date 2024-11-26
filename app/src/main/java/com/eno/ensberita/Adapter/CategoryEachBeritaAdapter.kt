package com.eno.ensberita.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eno.ensberita.databinding.ViewholderCategoryBinding

class CategoryEachBeritaAdapter(private val items: List<String>) :
    RecyclerView.Adapter<CategoryEachBeritaAdapter.ViewHolder>() {

    // Ubah 'Viewholder' ke 'ViewHolder' sesuai konvensi penamaan
    class ViewHolder(val binding: ViewholderCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        // Gunakan LayoutInflater dengan parameter yang jelas
        val binding = ViewholderCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Pastikan data dipasang dengan aman
        holder.binding.titleTxt.text = items[position]
    }

    override fun getItemCount(): Int = items.size
}
