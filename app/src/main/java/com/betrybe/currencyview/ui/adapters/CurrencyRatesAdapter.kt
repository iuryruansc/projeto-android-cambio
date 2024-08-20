package com.betrybe.currencyview.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.betrye.currencyview.databinding.CurrencyRecycleItemBinding

class CurrencyRatesAdapter(
    private val rates: Map<String, String>,
    private val codes: List<String>
) : RecyclerView.Adapter<CurrencyRatesAdapter.RatesViewHolder>() {

    class RatesViewHolder(private val binding: CurrencyRecycleItemBinding) :
        ViewHolder(binding.root) {
        fun bind(title: String, body: String) {
            binding.recycleItemTitle.text = title
            binding.recycleItemBody.text = body
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val binding =
            CurrencyRecycleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RatesViewHolder(binding)
    }

    override fun getItemCount(): Int = rates.size

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        val title = "${rates.keys.elementAt(position)} - ${codes[position]}"
        val body = rates.values.elementAt(position)
        holder.bind(title, body)
    }
}
