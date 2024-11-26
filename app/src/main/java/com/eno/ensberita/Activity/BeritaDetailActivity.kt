package com.eno.ensberita.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.eno.ensberita.Adapter.CategoryEachBeritaAdapter
import com.eno.ensberita.Models.Berita
import com.eno.ensberita.databinding.ActivityBeritaDetailBinding
import com.google.firebase.database.FirebaseDatabase
import eightbitlab.com.blurview.RenderScriptBlur

class BeritaDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBeritaDetailBinding
    private lateinit var berita: Berita

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBeritaDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEdit.setOnClickListener {
            val beritaId: Berita? = intent.getParcelableExtra("object")
            beritaId?.let {
                val intent = Intent(this, EditActivity::class.java)
                intent.putExtra("id", it.id) // Kirim ID berita
                startActivity(intent)
            } ?: run {
                Toast.makeText(this, "Data berita tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnHapus.setOnClickListener {
            val beritaId: Berita? = intent.getParcelableExtra("object")
            beritaId?.let { berita ->
                // Tampilkan alert dialog konfirmasi
                AlertDialog.Builder(this)
                    .setTitle("Hapus Berita")
                    .setMessage("Yakin ingin menghapus berita ini?")
                    .setPositiveButton("Ya") { _, _ ->
                        // Hapus data dari Firebase berdasarkan ID berita
                        val database = FirebaseDatabase.getInstance("https://ensberita-default-rtdb.asia-southeast1.firebasedatabase.app/")
                            .getReference("BeritaTerbaru")

                        database.child(berita.id!!).removeValue()
                            .addOnSuccessListener {
                                Toast.makeText(this, "Berita berhasil dihapus!", Toast.LENGTH_SHORT).show()
                                finish() // Tutup aktivitas setelah penghapusan berhasil
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Gagal menghapus berita: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .setNegativeButton("Batal", null) // Tombol batal tanpa aksi
                    .show()
            } ?: run {
                Toast.makeText(this, "Data berita tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setVariable()
    }

    private fun setVariable() {
        // Ambil objek Berita dari intent
        val beritaId: Berita? = intent.getParcelableExtra("object")

        // Periksa apakah beritaId tidak null, lalu bind data
        beritaId?.let {
            bindData(it.title, it.poster, it.imdb, it.year, it.time, it.description, it.category)
        }

        // Setup tombol kembali
        binding.backBtn.setOnClickListener {
            finish()
        }

        // Setup blur view
        val decorView = window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
        val windowsBackground = decorView.background

        binding.blurView.setupWith(rootView, RenderScriptBlur(this))
            .setFrameClearDrawable(windowsBackground)
            .setBlurRadius(10f)
        binding.blurView.outlineProvider = ViewOutlineProvider.BACKGROUND
        binding.blurView.clipToOutline = true
    }

    private fun bindData(
        Title: String?,
        Poster: String?,
        Imdb: Int,
        Year: Int,
        Time: String?,
        Description: String?,
        Category: List<String>?
    ) {
        Glide.with(this)
            .load(Poster)
            .apply(RequestOptions().transform(CenterCrop(), GranularRoundedCorners(0f, 0f, 50f, 50f)))
            .into(binding.beritaPic)

        binding.titleTxt.text = Title
        binding.imdbTxt.text = "IMDB $Imdb"
        binding.beritaTimeTxt.text = "$Year - $Time"
        binding.beritaSummeryTxt.text = Description

        Category?.let {
            binding.categoryView.adapter = CategoryEachBeritaAdapter(it)
            binding.categoryView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    companion object {
        fun createIntent(context: Context, berita: Berita): Intent {
            val intent = Intent(context, BeritaDetailActivity::class.java)
            intent.putExtra("object", berita)
            return intent
        }
    }
}
