package com.eno.ensberita.Activity

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eno.ensberita.Models.Berita
import com.eno.ensberita.databinding.ActivityTambahBinding // Import binding
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

class TambahActivity : AppCompatActivity() {

    // Deklarasi binding
    private lateinit var binding: ActivityTambahBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Daftar Kategori
        val kategoriList = listOf("Teknologi", "Olahraga", "Politik", "Hiburan", "Kesehatan")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kategoriList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerKategori.adapter = adapter

        // Tombol Simpan (Tambah Berita)
        binding.btnSimpan.setOnClickListener {
            val judul = binding.etJudul.text.toString().trim()
            val deskripsi = binding.etDeskripsi.text.toString().trim()
            val poster = binding.etPoster.text.toString().trim()
            val waktu = binding.etWaktu.text.toString().trim()
            val imdbText = binding.etImdb.text.toString().trim()
            val yearText = binding.etYear.text.toString().trim()
            val kategori = binding.spinnerKategori.selectedItem.toString()

            // Validasi input kosong
            if (judul.isEmpty() || deskripsi.isEmpty() || poster.isEmpty() || waktu.isEmpty() || imdbText.isEmpty() || yearText.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Konversi IMDB dan Year ke Int, dengan validasi
            val imdb = imdbText.toIntOrNull()
            val year = yearText.toIntOrNull()

            if (imdb == null || year == null) {
                Toast.makeText(this, "IMDB dan Tahun harus berupa angka!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Buat ID unik dan objek Berita
            val beritaId = UUID.randomUUID().toString()
            val berita = Berita(
                id = beritaId,
                title = judul,
                description = deskripsi,
                poster = poster,
                time = waktu,
                imdb = imdb,
                year = year,
                category = arrayListOf(kategori),
            )

            // Simpan data ke Firebase di tabel 'berita'
            val databaseReference =
                FirebaseDatabase.getInstance("https://ensberita-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("BeritaTerbaru")
            databaseReference.child(beritaId).setValue(berita)
                .addOnSuccessListener {
                    Log.d("TambahActivity", "Berita berhasil ditambahkan!")
                    Toast.makeText(this, "Berita berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                    finish() // Tutup aktivitas setelah berhasil
                }
                .addOnFailureListener { e ->
                    Log.e("TambahActivity", "Gagal menambahkan berita", e)
                    Toast.makeText(
                        this,
                        "Gagal menambahkan berita: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}
