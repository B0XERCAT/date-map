package edu.skku.cs.datemap

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ItemAdapter(private val context: Context, private val items: MutableList<DataModel.Item>) :
    BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.item, null)

        val categoryTextView = view.findViewById<TextView>(R.id.categoryText)
        val titleTextView = view!!.findViewById<TextView>(R.id.titleText)
        val addressTextView = view.findViewById<TextView>(R.id.addressText)
        val starButton = view.findViewById<ImageView>(R.id.starButton)

        val category = when {
            items[position].category == null -> {
                val newCategory = when (position) {
                    0 -> "맛집"
                    1 -> "카페"
                    2 -> "명소"
                    else -> "기타"
                }
                items[position].category = newCategory
                newCategory
            }

            else -> items[position].category
        }
        categoryTextView.text = category
        titleTextView.text = items[position].title
        addressTextView.text = items[position].address

        val isFavorite = checkIfFavorite(items[position])
        val starImageResource =
            if (isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_empty
        starButton.setImageResource(starImageResource)

        starButton.setOnClickListener {
            toggleFavorite(items[position])
            val newIsFavorite = checkIfFavorite(items[position])
            val newStarResource =
                if (newIsFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_empty
            starButton.setImageResource(newStarResource)
        }

        view.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(items[position].link))
            context.startActivity(intent)
        }

        return view
    }

    private fun checkIfFavorite(item: DataModel.Item): Boolean {
        val sharedPreferences = context.getSharedPreferences("LocalStorage", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("saved_items", null)
        val type = object : TypeToken<MutableList<DataModel.Item>>() {}.type
        val savedItems: MutableList<DataModel.Item> = gson.fromJson(json, type) ?: mutableListOf()

        return savedItems.contains(item)
    }

    private fun toggleFavorite(item: DataModel.Item) {
        val sharedPreferences = context.getSharedPreferences("LocalStorage", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("saved_items", null)
        val type = object : TypeToken<MutableList<DataModel.Item>>() {}.type
        val savedItems: MutableList<DataModel.Item> = gson.fromJson(json, type) ?: mutableListOf()

        if (savedItems.contains(item)) {
            savedItems.remove(item)
        } else {
            savedItems.add(item)
        }

        val updatedJson = gson.toJson(savedItems)
        val editor = sharedPreferences.edit()
        editor.putString("saved_items", updatedJson)
        editor.apply()

        val message = if (savedItems.contains(item)) "즐겨찾기 저장했습니다" else "즐겨찾기에서 제거했습니다"
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
