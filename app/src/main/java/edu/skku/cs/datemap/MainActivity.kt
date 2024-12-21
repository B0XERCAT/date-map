package edu.skku.cs.datemap

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val locationEditText: EditText = findViewById(R.id.locationEditText)
        val generateCourseButton: Button = findViewById(R.id.generateCourseButton)
        val viewSavedCoursesButton: Button = findViewById(R.id.viewSavedCoursesButton)

        generateCourseButton.setOnClickListener {
            val location = locationEditText.text.toString()
            val intent = Intent(this, LoadingActivity::class.java)
            intent.putExtra("location", location)
            startActivity(intent)
        }

        viewSavedCoursesButton.setOnClickListener {
            val intent = Intent(this, SavedPlacesActivity::class.java)
            startActivity(intent)
        }
    }
}