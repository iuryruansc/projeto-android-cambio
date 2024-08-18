package com.betrybe.currencyview.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.betrye.currencyview.R

class CurrencyRatesAdapter(private val rates: Map<String, String>, private val codes: List<String>) : RecyclerView.Adapter<CurrencyRatesAdapter.RatesViewHolder>() {

        class RatesViewHolder(view: View) : ViewHolder(view) {
            val title: TextView = view.findViewById(R.id.recycle_item_title)
            val body: TextView = view.findViewById(R.id.recycle_item_body)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.currency_recycle_item, parent, false)
        return RatesViewHolder(view)
    }

    override fun getItemCount(): Int = rates.size

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        holder.title.text = "${rates.keys.elementAt(position)} - ${codes[position]}"
        holder.body.text = rates.values.elementAt(position)
    }
}
