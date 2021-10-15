package com.abduqahhor.qr_task

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.abduqahhor.qr_task.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}