package com.example.tma_semestralka

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tma_semestralka.databinding.ActivityMainBinding
import com.example.tma_semestralka.player.PlayersFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, PlayersFragment())
                .commit()
        }
    }
}