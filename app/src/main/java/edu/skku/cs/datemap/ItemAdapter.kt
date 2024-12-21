package edu.skku.cs.datemap

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson

class ItemAdapter(private val context: Context, private val items: MutableList<DataModel.Item>) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.item, null)

        val categoryTextView = view.findViewById<TextView>(R.id.categoryText)
        val titleTextView = view!!.findViewById<TextView>(R.id.titleText)
        val addressTextView = view.findViewById<TextView>(R.id.addressText)
        val heartButton = view.findViewById<Button>(R.id.heartButton)

        val category = when (position) {
            0 -> "맛집"
            1 -> "카페"
            2 -> "명소"
            else -> "기타"
        }
        categoryTextView.text = category
        titleTextView.text = items[position].title
        addressTextView.text = items[position].address

        heartButton.setOnClickListener {
            saveItemToLocalStorage(items[position])
        }

        view.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(items[position].link))
            context.startActivity(intent)
        }

        return view
    }

    private fun saveItemToLocalStorage(item: DataModel.Item) {
        val sharedPreferences = context.getSharedPreferences("savedItems", Context.MODE_PRIVATE)
        val gson = Gson()

        val itemsJson = sharedPreferences.getString("items", "[]")
        val items = gson.fromJson(itemsJson, Array<DataModel.Item>::class.java).toMutableList()

        if (!items.contains(item)) {
            items.add(item)
            val updatedItemsJson = gson.toJson(items)
            sharedPreferences.edit().putString("items", updatedItemsJson).apply()
            Toast.makeText(context, "장소가 저장되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "이미 저장된 장소입니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
