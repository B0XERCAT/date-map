package edu.skku.cs.datemap

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ItemAdapter(private val context: Context, private val items: ArrayList<DataModel.Item>) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.item, null)

        val categoryTextView = view.findViewById<TextView>(R.id.categoryText)
        val titleTextView = view!!.findViewById<TextView>(R.id.titleText)
        val addressTextView = view.findViewById<TextView>(R.id.addressText)

        val category = when (position) {
            0 -> "맛집"
            1 -> "카페"
            2 -> "명소"
            else -> "기타"
        }
        categoryTextView.text = category
        titleTextView.text = items[position].title
        addressTextView.text = items[position].address

        view.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(items[position].link))
            context.startActivity(intent)
        }

        return view
    }
}
