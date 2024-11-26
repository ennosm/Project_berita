package com.eno.ensberita.Activity

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eno.ensberita.Models.Berita
import com.eno.ensberita.databinding.ActivityEditBinding
import com.google.firebase.database.*

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private lateinit var database: DatabaseReference
    private lateinit var kategoriList: List<String>
    private var beritaId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize database reference
        database = FirebaseDatabase.getInstance("https://ensberita-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("BeritaTerbaru")

        // Get the 'id' from the intent and set it to the class variable
        beritaId = intent.getStringExtra("id")

        if (beritaId == null) {
            Toast.makeText(this, "ID berita tidak ditemukan!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Populate the spinner with categories
        kategoriList = listOf("Politik", "Olahraga", "Teknologi", "Hiburan", "Kesehatan")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kategoriList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerKategori.adapter = spinnerAdapter

        // Load data for the news item if the ID is available
        loadBeritaData(beritaId!!)

        // Add listener for the 'edit' button
        binding.btnEdit.setOnClickListener {
            updateBeritaData()
        }
    }

    private fun loadBeritaData(beritaId: String) {
        // Load the news data from Firebase based on the 'id'
        database.child(beritaId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val berita = snapshot.getValue(Berita::class.java)
                    if (berita != null) {
                        binding.etJudul.setText(berita.title ?: "")
                        binding.etDeskripsi.setText(berita.description ?: "")
                        binding.etPoster.setText(berita.poster ?: "")
                        binding.etWaktu.setText(berita.time ?: "")
                        binding.etYear.setText(berita.year?.toString() ?: "")
                        binding.etImdb.setText(berita.imdb?.toString() ?: "")

                        // Menangani kategori yang berupa ArrayList<String>
                        if (berita.category.isNotEmpty()) {
                            val selectedCategory = berita.category.firstOrNull() ?: ""
                            val kategoriIndex = kategoriList.indexOf(selectedCategory)
                            if (kategoriIndex >= 0) {
                                binding.spinnerKategori.setSelection(kategoriIndex)
                            }
                        }
                    } else {
                        Toast.makeText(this@EditActivity, "Data kosong!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditActivity, "Gagal mengambil data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun updateBeritaData() {
        val updatedJudul = binding.etJudul.text.toString().trim()
        val updatedDeskripsi = binding.etDeskripsi.text.toString().trim()
        val updatedPoster = binding.etPoster.text.toString().trim()
        val updatedWaktu = binding.etWaktu.text.toString().trim()
        val updatedYear = binding.etYear.text.toString().toIntOrNull() ?: 0
        val updatedImdb = binding.etImdb.text.toString().toIntOrNull() ?: 0
        val updatedCategory = binding.spinnerKategori.selectedItem.toString()

        // Validasi input
        if (updatedYear == 0) {
            Toast.makeText(this, "Tahun tidak valid!", Toast.LENGTH_SHORT).show()
            return
        }

        if (updatedImdb == 0) {
            Toast.makeText(this, "Rating IMDB tidak valid!", Toast.LENGTH_SHORT).show()
            return
        }

        if (updatedJudul.isEmpty() || updatedDeskripsi.isEmpty() || updatedPoster.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        // Membuat map data terbaru untuk memperbarui berita
        val updatedBerita = mapOf(
            "title" to updatedJudul,
            "description" to updatedDeskripsi,
            "poster" to updatedPoster,
            "time" to updatedWaktu,
            "year" to updatedYear,
            "imdb" to updatedImdb,
            "category" to arrayListOf(updatedCategory)  // Simpan kategori sebagai ArrayList<String>
        )

        // Memperbarui data di Firebase berdasarkan ID yang sudah ada
        database.child(beritaId!!).updateChildren(updatedBerita)
            .addOnSuccessListener {
                Toast.makeText(this, "Data berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal memperbarui data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


}