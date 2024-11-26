package com.eno.ensberita.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.eno.ensberita.R
import com.google.firebase.auth.FirebaseAuth
import android.content.SharedPreferences

class LoginActivity : AppCompatActivity() {

    // Deklarasi variabel untuk komponen UI
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    private val LOGIN_TIME_KEY = "login_time"
    private val USERNAME_KEY = "username"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Inisialisasi FirebaseAuth dan SharedPreferences
        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)

        // Atur padding sesuai dengan sistem bar (status dan navigasi)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi komponen UI
        usernameEditText = findViewById(R.id.editTextText)
        passwordEditText = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.loginBtn)

        // Aksi untuk tombol login
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username atau password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                // Hanya izinkan login untuk admin
                if (username == "admin" && password == "1234") {
                    Toast.makeText(this, "Login sebagai Admin berhasil!", Toast.LENGTH_SHORT).show()
                    saveLoginTime()
                    saveUsername(username)
                    // Navigasi ke MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Username atau password salah", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveLoginTime() {
        // Simpan waktu login sekarang ke SharedPreferences
        sharedPreferences.edit().putLong(LOGIN_TIME_KEY, System.currentTimeMillis()).apply()
    }

    private fun saveUsername(username: String) {
        // Simpan username ke SharedPreferences
        sharedPreferences.edit().putString(USERNAME_KEY, username).apply()
    }
}
