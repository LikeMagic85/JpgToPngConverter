package com.like_magic.jpgtopngconverter.presentation

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.like_magic.jpgtopngconverter.R
import com.like_magic.jpgtopngconverter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ConverterContract.View {

    private lateinit var binding: ActivityMainBinding
    private val presenter = Presenter()
    private var pathImagePicked: String? = null
    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter.attach(this)
        binding.buttonConvert.setOnClickListener {
            openImage()
        }
    }

    override fun openImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpg"))
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            REQUEST_CODE_GET_CONTENT
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK &&
            requestCode == REQUEST_CODE_GET_CONTENT &&
            data != null
        ) {
            val imagePickedUri = data.data
            if (imagePickedUri != null) {
                pathImagePicked = getPathFromUri(imagePickedUri)
                binding.image.setImageURI(imagePickedUri)
                bitmap = (binding.image.drawable as BitmapDrawable).bitmap
                presenter.convert(bitmap!!, pathImagePicked!!)

            }
        }
    }

    private fun getPathFromUri(contentUri: Uri): String? {
        var res: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(projection[0])
            columnIndex.let {
                res = cursor.getString(columnIndex)
            }
            cursor.close()
        }
        return res
    }

    override fun showSnackBar(message: String, state: Int) {
        when (state) {
            0 -> {
                Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
                    .setAction(R.string.cancel) {
                        presenter.cancel()
                    }
                    .show()
            }
            1 -> {
                Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
                    .show()
            }
            2 -> {
                Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
                    .setAction(R.string.cancel) {
                        presenter.convert(bitmap!!, pathImagePicked!!)
                    }
                    .show()
            }
        }
    }

    override fun setNewImage(bitmap: Bitmap) {
        binding.image.setImageBitmap(bitmap)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }

    companion object {
        const val REQUEST_CODE_GET_CONTENT = 123
    }
}