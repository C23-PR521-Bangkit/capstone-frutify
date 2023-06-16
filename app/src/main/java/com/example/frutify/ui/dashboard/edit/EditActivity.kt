package com.example.frutify.ui.dashboard.edit

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.frutify.MainActivity
import com.example.frutify.R
import com.example.frutify.data.model.ProductItem
import com.example.frutify.data.viewmodel.ProductViewModel
import com.example.frutify.databinding.ActivityEditBinding
import com.example.frutify.ui.dashboard.edit.camera.CameraActivity
import com.example.frutify.utils.*
import java.io.File

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private lateinit var productViewModel: ProductViewModel
    private lateinit var sharePref: SharePref
    private var getFile: File? = null
    private var filename: String? = null
    private var quality: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        sharePref = SharePref(this)

        //ask permissions
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        // Dropdown
        val spinnerFruit: Spinner = binding.etFruit
        // Item Dropdown
        val fruitOptions: ArrayList<String> = ArrayList()
        fruitOptions.add("Apel")
        fruitOptions.add("Pisang")
        fruitOptions.add("Jeruk")
        val fruitIdArray = arrayOf(1, 2, 3)

        val fruitAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, fruitOptions)
        fruitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFruit.adapter = fruitAdapter

        val idFruit = spinnerFruit.selectedItemPosition

        //receive data from homesellerfragment //detail
        val product = intent.getParcelableExtra<ProductItem>("product")
        val fromHomeSeller = intent.getBooleanExtra("from_home_seller", false)
        val fromBtnDelete = intent.getBooleanExtra("from_btn_delete", false)
        val fruitId = product?.FRUITID
        val position = fruitIdArray.indexOf(fruitId)

        binding.previewImage.setOnClickListener {
            if (fromHomeSeller) {
                val intent = Intent(this, CameraActivity::class.java)
                intent.putExtra("fromHomeSeller", true) // Mengirim data fromHomeSeller
                launcherIntentCameraX.launch(intent)
            } else {
                startCameraX()
            }
        }

        val imageUrl = Constant.BASE_URL_2 +"uploads?path=" +  product?.PRODUCTFILEPATH


        if (fromHomeSeller) {
            binding.apply {
                btnUpdate.visibility = View.VISIBLE
                btnSave.visibility = View.GONE
                btnDelete.visibility = View.GONE

                etFruit.setSelection(position)
                etName.setText(product?.PRODUCTNAME)
                etDesc.setText(product?.PRODUCTDESCRIPTION)
                etPrice.setText(product?.PRODUCTPRICE!!.toString())
                tvQuality.text = "Quality : ${product.PRODUCTQUALITY}"
                Log.e("erce", tvQualityResult.toString())
                Glide.with(this@EditActivity)
                    .load(imageUrl)
                    .error(R.drawable.apel)
                    .into(binding.previewImage)

            }
        } else if (fromBtnDelete) {
            binding.apply {
                btnUpdate.visibility = View.GONE
                btnSave.visibility = View.GONE
                btnDelete.visibility = View.VISIBLE


                etFruit.setSelection(position)
                etName.setText(product?.PRODUCTNAME)
                etDesc.setText(product?.PRODUCTDESCRIPTION)
                etPrice.setText(product?.PRODUCTPRICE!!.toString())
                tvQuality.text = "Quality : ${product.PRODUCTQUALITY}"

                Glide.with(this@EditActivity)
                    .load(imageUrl)
                    .error(R.drawable.apel)
                    .into(binding.previewImage)
            }
        } else {
            binding.btnSave.visibility = View.VISIBLE
        }

        //receive file from intent galery
        val myFile = intent.getSerializableExtra("pictureUri") as? File
        val ImageBitmap = BitmapFactory.decodeFile(myFile?.path)

        binding.previewImage.setImageBitmap(ImageBitmap)

        filename = intent.getStringExtra(EXTRA_FILENAME)
        quality = intent.getStringExtra(EXTRA_QUALITY)
        binding.tvQualityResult.setText(quality)
        binding.btnSave.setOnClickListener {

            if (validateFields()) {
                val fruit = fruitIdArray[idFruit]
                val userId = sharePref.getUserId
                val name = binding.etName.text.toString().trim()
                val desc = binding.etDesc.text.toString().trim()
                val price = binding.etPrice.text.toString().toInt()
                val unit = "kg"


                if (filename != null) {
                    addProduct(fruit, userId, name, desc, price, unit, filename!!, quality!!)
                } else {
                    Toast.makeText(this, "Tolong Masukan Gambar", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnUpdate.setOnClickListener {

            if(validateFields()) {
                val productId = product?.PRODUCTID
                val fruit = fruitIdArray[idFruit]
                val userId = sharePref.getUserId
                val name = binding.etName.text.toString().trim()
                val desc = binding.etDesc.text.toString().trim()
                val price = binding.etPrice.text.toString().toInt()
                val unit = "kg"
                val filenameUpdate = product?.PRODUCTFILEPATH
                val qualityUpdate = product?.PRODUCTQUALITY

                val updatedFilename = filename ?: filenameUpdate
                val updatedQuality = quality ?: qualityUpdate

                updateProduct(
                    productId!!,
                    fruit,
                    userId,
                    name,
                    desc,
                    price,
                    unit,
                    updatedFilename!!,
                    updatedQuality!!
                )
            }
        }

        binding.btnDelete.setOnClickListener {

            val product_id = product?.PRODUCTID
            val user_id = sharePref.getUserId

            openDeleteDialog(product_id!!, user_id)
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val data = it.data

        if (it.resultCode == CAMERA_X_RESULT) {
            val dataFileName = data?.getStringExtra(EXTRA_FILENAME)
            dataFileName.let {
                filename = it
            }
            val dataQuality = data?.getStringExtra(EXTRA_QUALITY)
            dataQuality.let {
                quality = it
                binding.tvQualityResult.setText(quality)
            }
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                binding.previewImage.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private fun addProduct(
        fruit_id: Int,
        user_id: Int,
        name: String,
        description: String,
        price: Int,
        unit: String,
        filename: String,
        quality: String
    ) {
        productViewModel.addProduct(
            fruit_id,
            user_id,
            name,
            description,
            price,
            unit,
            filename,
            quality
        )
        loadingAndError()
        productViewModel.addProductResult.observe(this) {
            Toast.makeText(this, it?.MESSAGE, Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun updateProduct(
        product_id: Int,
        fruit_id: Int,
        user_id: Int,
        name: String,
        description: String,
        price: Int,
        unit: String,
        filename: String,
        quality: String
    ) {
        productViewModel.updateProduct(
            product_id,
            fruit_id,
            user_id,
            name,
            description,
            price,
            unit,
            filename,
            quality
        )
        loadingAndError()
        productViewModel.updateProductResult.observe(this) {
            Toast.makeText(this, it?.MESSAGE, Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun deleteProduct(product_id: Int, user_id: Int) {
        productViewModel.deleteProduct(product_id, user_id)
        loadingAndError()
        productViewModel.deleteProductResult.observe(this) {
            Toast.makeText(this, it?.MESSAGE, Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun loadingAndError() {
        productViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        productViewModel.error.observe(this) { error ->
            // Show error message
            //bdrl
            //Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateFields(): Boolean {
        if ((binding.etName.text?.length ?: 0) <= 0) {
            binding.etName.error = getString(R.string.errorfield)
            binding.etName.requestFocus()
            return false
        } else if ((binding.etDesc.text?.length ?: 0) <= 0) {
            binding.etDesc.error = getString(R.string.errorfield)
            binding.etDesc.requestFocus()
            return false
        } else if ((binding.etPrice.text?.length ?: 0) <= 0) {
            binding.etPrice.error = getString(R.string.errorfield)
            binding.etPrice.requestFocus()
            return false
        }
        return true
    }

    private fun openDeleteDialog(productId: Int, userId: Int) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Confirm Delete")
            ?.setPositiveButton("yes") { _, _ ->


                deleteProduct(productId!!, userId)
            }
            ?.setNegativeButton("no", null)
        val alert = alertDialog.create()
        alert.show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        const val EXTRA_FILENAME = "filename"
        const val EXTRA_QUALITY = "quality"
    }
}