package edu.skku.cs.giftizone.addGifticon

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import edu.skku.cs.giftizone.R
import edu.skku.cs.giftizone.dataClass.Gifticon
import edu.skku.cs.giftizone.enums.SortFilter
import java.time.LocalDate
import java.util.*

class AddGifticonActivity : AppCompatActivity() {
    private var tagList: ArrayList<String>? = null
    private var selectedTag: String? = null
    private var expiredDate: LocalDate? = null
    private val pickImageResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        val gifticonImage = findViewById<ImageView>(R.id.addGifticonImage)
        if (uri != null) {
            Glide.with(this)
                .load(uri)  // or Uri or File
                .into(gifticonImage)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gifticon)
        tagList = intent.getStringArrayListExtra("tagList")

        pickImageFromGallery()
        setupSelectExpireDate()
        setupTagSelectDropdown()
    }

    private fun pickImageFromGallery() {
        pickImageResultLauncher.launch("image/*")
    }

    private fun setupSelectExpireDate() {
        val selectExpireDateBtn = findViewById<ImageView>(R.id.calenderButton)
        val expireDateText = findViewById<TextView>(R.id.addExpireText)
        selectExpireDateBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                expireDateText.text = "$selectedYear/${selectedMonth+1}/$selectedDay"
            }, year, month, day)
            expiredDate = LocalDate.of(year, month+1, day)

            datePickerDialog.show()
        }
    }

    private fun setupTagSelectDropdown() {
        val addTagDropdown: Spinner = findViewById(R.id.addTagDropdown)

        addTagDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedTag = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedTag = parent.getItemAtPosition(0).toString()
            }
        }

        ArrayAdapter(this,
            android.R.layout.simple_spinner_item,
            tagList!!)
            .also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            addTagDropdown.adapter = adapter
        }
    }

    private fun isValidGifticon(): Boolean {
        val barcode = findViewById<EditText>(R.id.barcodeEdit).text.toString()
        if (barcode.isEmpty()) {
            toast("바코드를 입력해주세요.")
            return false
        }

        val gifticonProvider = findViewById<EditText>(R.id.providerEdit).text.toString()
        if (gifticonProvider.isEmpty()) {
            toast("사용처를 입력해주세요.")
            return false
        }

        val gifticonContent = findViewById<EditText>(R.id.contentEdit).text.toString()
        if (gifticonContent.isEmpty()) {
            toast("상품명을 입력해주세요.")
            return false
        }

        if (expiredDate == null) {
            toast("유효기간을 선택해주세요.")
            return false
        }
        
        if (selectedTag == null) {
            toast("태그를 선택해주세요.")
            return false
        }
        return true
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}