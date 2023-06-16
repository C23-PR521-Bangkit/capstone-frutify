package com.example.frutify.ui.dashboard.edit.camera

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.frutify.data.viewmodel.ClasifyViewModel
import com.example.frutify.databinding.ActivityCameraBinding
import com.example.frutify.ui.dashboard.edit.EditActivity
import com.example.frutify.utils.Constant
import com.example.frutify.utils.Utility
import com.example.frutify.utils.uriToFile
import java.io.FileOutputStream

class CameraActivity : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private lateinit var binding: ActivityCameraBinding
    private lateinit var clasifyViewModel: ClasifyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clasifyViewModel = ViewModelProvider(this)[ClasifyViewModel::class.java]

        binding.btnCapture.setOnClickListener { takePhoto() }
        binding.switchCamera.setOnClickListener {
            cameraSelector = if (cameraSelector.equals(CameraSelector.DEFAULT_BACK_CAMERA)) CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA

            startCamera()
        }
        val fromHomeSeller = intent.getBooleanExtra("fromHomeSeller", false)

        if (fromHomeSeller) {
            binding.btnGallery.isEnabled = false
            binding.btnGallery.alpha = 0f

            binding.btnGallery.setOnClickListener(null) // Menonaktifkan respons klik
        }

        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnBack.setOnClickListener { onBackPressed() }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraActivity,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = com.example.frutify.utils.createFile(application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val rotatedBitmap = Utility.rotateBitmap(BitmapFactory.decodeFile(photoFile.path), cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                    val rotatedFile = createTempFile("rotated_", ".jpg", cacheDir)
                    rotatedFile.deleteOnExit()
                    val outputStream = FileOutputStream(rotatedFile)
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.close()

                    clasifyViewModel.predictImage(Constant.BASE_URL_2 ,rotatedFile)
                    clasifyViewModel.isLoading.observe(this@CameraActivity){ showLoading(it) }
                    clasifyViewModel.imagePredictResult.observe(this@CameraActivity) { imageClasifyResponse ->
                        val filename = imageClasifyResponse?.PAYLOAD?.filename.toString()
                        val persent = imageClasifyResponse?.PAYLOAD?.precentage
                        val precentage = persent?.times(100)
                        val quality = imageClasifyResponse?.PAYLOAD?.quality.toString()
                        val qualityResult = "$quality ${precentage?.toInt()}%"
                        val intentRes = Intent(this@CameraActivity, EditActivity::class.java)
                        intentRes.putExtra(EditActivity.EXTRA_FILENAME, filename)
                        intentRes.putExtra(EditActivity.EXTRA_QUALITY, qualityResult)
                        intentRes.putExtra("picture", photoFile)
                        intentRes.putExtra(
                            "isBackCamera",
                            cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA
                        )
                        setResult(EditActivity.CAMERA_X_RESULT, intentRes)
                        finish()
                    }
                }
            }
        )
    }

    //intent galery
    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImageUri = result.data?.data as Uri
            selectedImageUri.let { uri ->
                val myFile = uriToFile(uri, this)
                clasifyViewModel.predictImage(Constant.BASE_URL_2 ,myFile)
                clasifyViewModel.isLoading.observe(this@CameraActivity){ showLoading(it) }
                clasifyViewModel.imagePredictResult.observe(this){
                    val filename = it?.PAYLOAD?.filename.toString()
                    val quality = it?.PAYLOAD?.quality.toString()
                    val persent = it?.PAYLOAD?.precentage
                    val precentage = persent?.times(100)
                    val qualityResult = "$quality ${precentage?.toInt()}%"
                    val intent = Intent(this, EditActivity::class.java)
                    intent.putExtra(EditActivity.EXTRA_FILENAME, filename)
                    intent.putExtra(EditActivity.EXTRA_QUALITY, qualityResult)
                    intent.putExtra("pictureUri", myFile)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}