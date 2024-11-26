package com.eno.ensberita.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.eno.ensberita.Adapter.BeritaListAdapter
import com.eno.ensberita.Adapter.PelangganAdapter
import com.eno.ensberita.Adapter.SliderAdapter
import com.eno.ensberita.Models.Berita
import com.eno.ensberita.Models.SliderItems
import com.eno.ensberita.R
import com.eno.ensberita.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: FirebaseDatabase
    private val sliderHandler = Handler()
    private val sliderRunnable = Runnable {
        binding.viewPager2.currentItem = binding.viewPager2.currentItem + 1
    }
    // Tambahkan referensi ke Firebase Auth
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity", "MainActivity is launched")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance()



        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "default")
        Log.d("MainActivity", "Username: $username")


        database=FirebaseDatabase.getInstance("https://ensberita-default-rtdb.asia-southeast1.firebasedatabase.app/")

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        initBanner()
        initBeritaTerbaru()
        ketambah()
    }

    private fun logoutUser() {
        // Tambahkan log untuk debugging
        Log.d("Logout", "Fungsi logout dipanggil")

        // Logout Firebase
        if (auth.currentUser != null) {
            auth.signOut()
        }

        // Verifikasi apakah user benar-benar sudah logout
        if (auth.currentUser == null) {
            Log.d("Logout", "User sudah logout")  // Ini seharusnya muncul di Logcat jika logout berhasil
        } else {
            Log.d("Logout", "User masih login")  // Jika ini muncul, logout belum berhasil
        }

        // Tampilkan pesan logout berhasil
        Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show()
    }



    private fun ketambah() {
        binding.btnTambahBerita.setOnClickListener {
            val intent = Intent(this, TambahActivity::class.java)
            startActivity(intent)
        }
    }


    private fun initBeritaTerbaru() {
        val myRef:DatabaseReference=database.getReference("BeritaTerbaru")
        binding.progressBarBeritaTerbaru.visibility=View.VISIBLE
        val items=ArrayList<Berita>()

        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (issue in snapshot.children){
                        items.add(issue.getValue(Berita::class.java)!!)
                    }
                    if (items.isNotEmpty()){
                        binding.recyclerViewBeritaTerbaru.layoutManager=LinearLayoutManager(
                            this@MainActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        binding.recyclerViewBeritaTerbaru.adapter=PelangganAdapter(items)
                    }
                    binding.progressBarBeritaTerbaru.visibility=View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                binding.progressBarBeritaTerbaru.visibility = View.GONE
                binding.progressBarSlider.visibility = View.GONE
                error.message?.let {
                    Log.e("FirebaseError", it)
                }
            }

        })
    }

    private fun initBanner() {
        val myRef: DatabaseReference = database.getReference("Banners")
        binding.progressBarSlider.visibility = View.VISIBLE

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderItems>() // Perbaikan: deklarasi di luar loop
                for (childSnapshot in snapshot.children) {
                    val item = childSnapshot.getValue(SliderItems::class.java) // Ubah variabel ini
                    if (item != null) {
                        lists.add(item)
                    }
                }
                binding.progressBarSlider.visibility = View.GONE
                banners(lists)
            }

            override fun onCancelled(error: DatabaseError) {
                // Tangani error sesuai kebutuhan
            }
        })
    }

    private fun banners(lists: MutableList<SliderItems>) {
        binding.viewPager2.adapter = SliderAdapter(lists, binding.viewPager2)
        binding.viewPager2.clipToPadding = false
        binding.viewPager2.clipChildren = false
        binding.viewPager2.offscreenPageLimit = 3
        binding.viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
            addTransformer(ViewPager2.PageTransformer { page, position ->
                val r = 1 - Math.abs(position)
                page.scaleY = 0.85f + r * 0.15f
            })
        }

        binding.viewPager2.setPageTransformer(compositePageTransformer)
        binding.viewPager2.currentItem=1
        binding.viewPager2.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int){
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
            }
        })

    }
}