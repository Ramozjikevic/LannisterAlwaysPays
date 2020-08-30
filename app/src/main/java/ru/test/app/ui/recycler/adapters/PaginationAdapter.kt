package ru.test.app.ui.recycler.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.test.app.R
import ru.test.app.features.infocharacter.CharacterInfoActivity
import ru.test.app.models.CharacterItemUI

class PaginationAdapter(private val downloadNewPageListener: () -> Unit) :
    RecyclerView.Adapter<PaginationAdapter.BasicViewHolder>() {

    var items: MutableList<CharacterItemUI> = mutableListOf()
    private var isLoaderVisible = false
    private var isErrorVisible = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasicViewHolder {
        return when (viewType) {
            VIEW_TYPE_NORMAL -> BasicViewHolder.CharactersViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_list_character,
                    parent,
                    false
                )
            )

            VIEW_TYPE_ERROR -> BasicViewHolder.ErrorHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_error,
                    parent,
                    false
                )
            )

            else -> BasicViewHolder.ProgressHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_loading,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == items.size - 1) {
            when {
                isLoaderVisible -> VIEW_TYPE_LOADING
                isErrorVisible -> VIEW_TYPE_ERROR
                else -> VIEW_TYPE_NORMAL
            }
        } else VIEW_TYPE_NORMAL
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BasicViewHolder, position: Int) {
        when (holder) {
            is BasicViewHolder.CharactersViewHolder -> holder.bind(items[position])
            is BasicViewHolder.ErrorHolder -> holder.bind(downloadNewPageListener, ::removeError)
        }
    }

    fun addItems(data: List<CharacterItemUI>) {
        items.addAll(data)
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    fun addLoading() {
        isLoaderVisible = true
        addHolder()
    }

    fun removeLoading() {
        isLoaderVisible = false
        removeHolder()
    }

    fun addError() {
        removeLoading()
        isErrorVisible = true
        addHolder()
    }

    private fun removeError() {
        isErrorVisible = false
        removeHolder()
    }

    private fun addHolder() {
        items.add(CharacterItemUI())
        notifyItemInserted(items.size - 1)
    }

    private fun removeHolder() {
        val position: Int = items.size - 1
        val item = getItem(position)
        if (item != null) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    private fun getItem(position: Int): CharacterItemUI? {
        return items[position]
    }

    sealed class BasicViewHolder(container: View) : RecyclerView.ViewHolder(container) {

        class CharactersViewHolder(container: View) : BasicViewHolder(container) {
            private val name: TextView = container.findViewById(R.id.name)
            fun bind(item: CharacterItemUI) {
                name.text = item.name
                itemView.setOnClickListener { view ->
                    item.id?.let { view.context.navigateToCharacterInfo(it) }
                }
            }
        }

        class ProgressHolder(container: View) : BasicViewHolder(container)

        class ErrorHolder(container: View) : BasicViewHolder(container) {
            private val bottomRetry: TextView = container.findViewById(R.id.bottomRetry)
            fun bind(downloadNewPageListener: () -> Unit, removeErrorListener: () -> Unit) {
                bottomRetry.setOnClickListener {
                    removeErrorListener.invoke()
                    downloadNewPageListener.invoke()
                }
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_LOADING = 0
        private const val VIEW_TYPE_NORMAL = 1
        private const val VIEW_TYPE_ERROR = 2
    }
}

fun Context.navigateToCharacterInfo(id: Long) {
    val intent = Intent(this, CharacterInfoActivity::class.java).apply {
        putExtra("id", id)
    }
    this.startActivity(intent)
}

