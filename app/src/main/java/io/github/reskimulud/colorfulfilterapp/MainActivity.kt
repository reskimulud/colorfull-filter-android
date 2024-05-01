package io.github.reskimulud.colorfulfilterapp

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import io.github.reskimulud.colorfulfilterapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null
    private lateinit var currentBitmap: Bitmap
    private lateinit var imageFiltering: ImageFiltering

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageFiltering = ImageFiltering()

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
                setLoading(true)
                lifecycleScope.launch {
                    getBitmapFromUri(currentImageUri)

                    imageFiltering.filterColorfulImageFromBitmap(currentBitmap) {
                        runOnUiThread {
                            ivPreviewImage.setImageBitmap(it)
                            setLoading(false)
                        }
                    }
                }
            }

            btnIsBlackWhite.setOnClickListener {
                setLoading(true)

                lifecycleScope.launch {
                    getBitmapFromUri(currentImageUri)

                    imageFiltering.isBlackAndWhite(currentBitmap) {
                        runOnUiThread {
                            if (it) {
                                Toast.makeText(this@MainActivity, "Ini adalah gambar hitam putih", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@MainActivity, "Ini bukan gambar hitam putih", Toast.LENGTH_SHORT).show()
                            }
                            setLoading(false)
                        }
                    }
                }
            }
        }
    }

    private fun initUI() {}

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        // TODO : camera
    }

    private fun getBitmapFromUri(uri: Uri?) {
        try {
            uri?.let {
                if (Build.VERSION.SDK_INT < 28) {
                    currentBitmap = MediaStore.Images.Media.getBitmap(
                        this.contentResolver,
                        uri
                    )
                } else {
                    val source = ImageDecoder.createSource(this.contentResolver, uri)
                    currentBitmap = ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                        decoder.setTargetSampleSize(1)
                        decoder.isMutableRequired = true
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivPreviewImage.setImageURI(it)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                btnFilter.isEnabled = false
                btnFromGallery.isEnabled = false
                btnTakeCamera.isEnabled = false
                pbLoading.visibility = View.VISIBLE
            } else {
                btnFilter.isEnabled = true
                btnFromGallery.isEnabled = true
                btnTakeCamera.isEnabled = true
                pbLoading.visibility = View.GONE
            }
        }
    }
}