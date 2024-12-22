package edu.skku.cs.datemap

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class SavedPlacesActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var emptyTextView: TextView
    private val savedItems = mutableListOf<DataModel.Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_places)

        listView = findViewById(R.id.placeListView)
        emptyTextView = findViewById(R.id.emptyTextView)

        loadSavedItems()

        if (savedItems.isEmpty()) {
            emptyTextView.visibility = View.VISIBLE
            listView.visibility = View.GONE
        } else {
            emptyTextView.visibility = View.GONE
            listView.visibility = View.VISIBLE
        }

        val adapter = ItemAdapter(this, savedItems)
        listView.adapter = adapter
    }

    private fun loadSavedItems() {
        val sharedPreferences = getSharedPreferences("LocalStorage", MODE_PRIVATE)
        val gson = Gson()

        val itemsJson = sharedPreferences.getString("saved_items", "[]")
        val items = gson.fromJson(itemsJson, Array<DataModel.Item>::class.java).toMutableList()

        savedItems.clear()
        savedItems.addAll(items)
    }
}