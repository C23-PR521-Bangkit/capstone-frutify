package com.example.frutify.ui.dashboard.edit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.frutify.R
import com.example.frutify.databinding.ActivityEditBinding
import com.example.frutify.ui.dashboard.camera.CameraActivity

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.storyImage.setOnClickListener { startActivity(Intent(this, CameraActivity::class.java)) }
    }
}