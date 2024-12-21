package edu.skku.cs.datemap

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class SavedPlacesActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private val savedItems = mutableListOf<DataModel.Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_places)

        listView = findViewById(R.id.placeListView)

        loadSavedItems()

        val adapter = ItemAdapter(this, savedItems)
        listView.adapter = adapter
    }

    private fun loadSavedItems() {
        val sharedPreferences = getSharedPreferences("savedItems", MODE_PRIVATE)
        val gson = Gson()

        val itemsJson = sharedPreferences.getString("items", "[]")
        val items = gson.fromJson(itemsJson, Array<DataModel.Item>::class.java).toMutableList()

        savedItems.clear()
        savedItems.addAll(items)
    }
}