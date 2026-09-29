package com.joshuadickey.findit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.joshuadickey.findit.databinding.ActivityAddItemBinding
import java.io.File
import java.io.FileOutputStream

class AddItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddItemBinding; private var photoPath: String? = null
    private val pick = registerForActivityResult(ActivityResultContracts.GetContent()) { uri -> uri?.let { saveUri(it) } }
    private val camera = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap -> bitmap?.let { saveBitmap(it) } }
    private val speech = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result -> if (result.resultCode == RESULT_OK) { val words = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull(); if (!words.isNullOrBlank()) binding.noteInput.setText(words) } }
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); binding = ActivityAddItemBinding.inflate(layoutInflater); setContentView(binding.root)
        binding.choosePhoto.setOnClickListener { pick.launch("image/*") }
        binding.takePhoto.setOnClickListener { if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) camera.launch(null) else requestPermissions(arrayOf(Manifest.permission.CAMERA), 10) }
        binding.voiceNote.setOnClickListener { speech.launch(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM).putExtra(RecognizerIntent.EXTRA_PROMPT, "Describe where you put it")) }
        binding.cancelButton.setOnClickListener { finish() }
        binding.saveButton.setOnClickListener { save() }
    }
    private fun save() { val name = binding.nameInput.text.toString().trim(); val location = binding.locationInput.text.toString().trim(); if (name.isBlank() || location.isBlank()) { Toast.makeText(this, "Add an item name and location.", Toast.LENGTH_SHORT).show(); return }; ItemStore(this).save(SavedItem(System.currentTimeMillis(), name, location, binding.noteInput.text.toString().trim(), photoPath)); finish() }
    private fun saveUri(uri: Uri) {
        val file = File(filesDir, "item_${System.currentTimeMillis()}.jpg")
        contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output -> input.copyTo(output) }
        }
        if (file.exists()) {
            photoPath = file.absolutePath
            binding.photoPreview.setImageURI(Uri.fromFile(file))
        }
    }
    private fun saveBitmap(bitmap: Bitmap) { val file = File(filesDir, "item_${System.currentTimeMillis()}.jpg"); FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 88, it) }; photoPath = file.absolutePath; binding.photoPreview.setImageBitmap(bitmap) }
}
