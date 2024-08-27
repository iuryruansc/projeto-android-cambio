package com.currencyview.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.currencyview.databinding.CurrencyItemBinding

class CurrencyArrayAdapter(context: Context, resource: Int, objects: List<String>) :
    ArrayAdapter<String>(context, resource, objects) {

    // ViewHolder class for each item in the dropdown menu
    class ViewHolder(private val binding: CurrencyItemBinding) {
        fun bind(currency: String) {
            binding.currencyItemText.text = currency // Bind the currency to the TextView
        }
    }

    // Inflating the layout for each item and creating a ViewHolder
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            val binding = CurrencyItemBinding.inflate(LayoutInflater.from(context), parent, false)
            viewHolder = ViewHolder(binding)
            view = binding.root
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.bind(getItem(position)!!) // Bind the currency to the ViewHolder
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

}
