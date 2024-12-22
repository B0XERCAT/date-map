package edu.skku.cs.datemap

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val locationEditText: EditText = findViewById(R.id.locationEditText)
        val generateCourseButton: Button = findViewById(R.id.generateCourseButton)
        val viewSavedPlacesButton: Button = findViewById(R.id.viewSavedPlacesButton)

        generateCourseButton.setOnClickListener {
            val location = locationEditText.text.toString()
            if (location.isEmpty()) {
                Toast.makeText(this, "지역을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, LoadingActivity::class.java)
                intent.putExtra("location", location)
                startActivity(intent)
                locationEditText.setText("")
            }
        }

        viewSavedPlacesButton.setOnClickListener {
            val intent = Intent(this, SavedPlacesActivity::class.java)
            startActivity(intent)
        }
    }
}