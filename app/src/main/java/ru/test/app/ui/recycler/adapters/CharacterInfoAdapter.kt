package ru.test.app.ui.recycler.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.test.app.R

class CharacterInfoAdapter : RecyclerView.Adapter<CharacterInfoAdapter.SingleLineViewHolder>() {

    private var items = mutableListOf<Pair<String, String>>()

    override fun onBindViewHolder(holder: SingleLineViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleLineViewHolder {
        return SingleLineViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_character_info, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    fun addItems(postItems: List<Pair<String, String>>) {
        items.addAll(postItems)
        notifyDataSetChanged()
    }

    class SingleLineViewHolder(container: View): RecyclerView.ViewHolder(container) {
        private val label: TextView = container.findViewById(R.id.label)
        private val value: TextView = container.findViewById(R.id.value)
        fun onBind(item: Pair<String, Any>) {
            label.text = item.first
            value.text = item.second.toString()
        }
    }
}
