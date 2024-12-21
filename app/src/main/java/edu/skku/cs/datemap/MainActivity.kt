package edu.skku.cs.datemap

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val locationEditText: EditText = findViewById(R.id.locationEditText)
        val generateCourseButton: Button = findViewById(R.id.generateCourseButton)
        val viewSavedCoursesButton: Button = findViewById(R.id.viewSavedCoursesButton)

        generateCourseButton.setOnClickListener {
            // 지역 입력 값을 가져와서 LoadingActivity로 넘김
            val location = locationEditText.text.toString()
            val intent = Intent(this, LoadingActivity::class.java)
            intent.putExtra("location", location)
            startActivity(intent)
        }

        viewSavedCoursesButton.setOnClickListener {
            val intent = Intent(this, SavedCoursesActivity::class.java)
            startActivity(intent)
        }
    }
}