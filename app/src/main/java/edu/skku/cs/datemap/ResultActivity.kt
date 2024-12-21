package edu.skku.cs.datemap

import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ResultActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        listView = findViewById(R.id.listView)
        saveButton = findViewById(R.id.saveButton)

        val restaurantJson = intent.getStringExtra("restaurant")
        val cafeJson = intent.getStringExtra("cafe")
        val attractionJson = intent.getStringExtra("attraction")

        if (restaurantJson == null || cafeJson == null || attractionJson == null) {
            Toast.makeText(this, "데이터가 누락되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val restaurant = Gson().fromJson(restaurantJson, DataModel.Item::class.java)
        val cafe = Gson().fromJson(cafeJson, DataModel.Item::class.java)
        val attraction = Gson().fromJson(attractionJson, DataModel.Item::class.java)

        val items = arrayListOf<DataModel.Item>()
        items.add(restaurant)
        items.add(cafe)
        items.add(attraction)

        val adapter = ItemAdapter(this, items)
        listView.adapter = adapter
        adapter.notifyDataSetChanged()

        saveButton.setOnClickListener {
            saveToLocalStorage(items)
            Toast.makeText(this, "성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // TODO: Implement the saveToLocalStorage function
    private fun saveToLocalStorage(items: MutableList<DataModel.Item>) {
        val sharedPreferences = getSharedPreferences("LocalStorage", MODE_PRIVATE)
        val gson = Gson()

        val json = sharedPreferences.getString("saved_items", null)
        val type = object : TypeToken<MutableList<DataModel.Item>>() {}.type
        val savedItems: MutableList<DataModel.Item> = gson.fromJson(json, type) ?: mutableListOf()

        savedItems.addAll(items)

        val updatedJson = gson.toJson(savedItems)
        val editor = sharedPreferences.edit()
        editor.putString("saved_items", updatedJson)
        editor.apply()

        Toast.makeText(this, "성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show()
    }
}
