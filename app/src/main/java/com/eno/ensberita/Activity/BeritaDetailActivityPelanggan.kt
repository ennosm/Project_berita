package com.eno.ensberita.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.eno.ensberita.Adapter.CategoryEachBeritaAdapter
import com.eno.ensberita.Models.Berita
import com.eno.ensberita.databinding.ActivityBeritaDetailPelangganBinding
import eightbitlab.com.blurview.RenderScriptBlur
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class BeritaDetailActivityPelanggan : AppCompatActivity() {
    private lateinit var binding: ActivityBeritaDetailPelangganBinding
    private lateinit var berita: Berita
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBeritaDetailPelangganBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)

        setVariable()
    }

    private fun setVariable() {
        // Ambil objek Berita dari intent
        berita = intent.getParcelableExtra<Berita>("object") ?: run {
            Toast.makeText(this, "Data berita tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Bind data berita
        bindData(berita)

        // Setup tombol kembali
        binding.backBtn.setOnClickListener {
            finish()
        }

        // Setup blur view
        setupBlurView()

    }

    private fun bindData(berita: Berita) {
        Glide.with(this)
            .load(berita.poster)
            .apply(RequestOptions().transform(CenterCrop(), GranularRoundedCorners(0f, 0f, 50f, 50f)))
            .into(binding.beritaPic)

        binding.titleTxt.text = berita.title
        binding.imdbTxt.text = "IMDB ${berita.imdb}"
        binding.beritaTimeTxt.text = "${berita.year} - ${berita.time}"
        binding.beritaSummeryTxt.text = berita.description

        berita.category?.let {
            binding.categoryView.adapter = CategoryEachBeritaAdapter(it)
            binding.categoryView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupBlurView() {
        val decorView = window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
        val windowsBackground = decorView.background

        binding.blurView.setupWith(rootView, RenderScriptBlur(this))
            .setFrameClearDrawable(windowsBackground)
            .setBlurRadius(10f)
        binding.blurView.outlineProvider = ViewOutlineProvider.BACKGROUND
        binding.blurView.clipToOutline = true
    }

    companion object {
        fun createIntent(context: Context, berita: Berita): Intent {
            val intent = Intent(context, BeritaDetailActivityPelanggan::class.java)
            intent.putExtra("object", berita)
            return intent
        }
    }
}