package io.github.reskimulud.colorfulfilterapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.github.reskimulud.colorfulfilterapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAction()
        initUI()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initAction() {
        binding.apply {
            btnFromGallery.setOnClickListener {
                startGallery()
            }

            btnTakeCamera.setOnClickListener {
                startCamera()
            }

            btnFilter.setOnClickListener {
                // TODO : filter
            }
        }
    }

    private fun initUI() {}

    private fun startGallery() {
        // TODO : gallery
    }

    private fun startCamera() {
        // TODO : camera
    }
}