package com.limitlesser.baserecyclerviewadapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView

class DiffItemCallback<T>(val id: T.() -> Any = { this!! },
                          val content: (T, T) -> Boolean = { o, n -> o == n },
                          val payload: (T, T) -> Any? = { _, _ -> null }) : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.id() == newItem.id()
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return content(oldItem, newItem)
    }

    override fun getChangePayload(oldItem: T, newItem: T): Any? {
        return payload(oldItem, newItem)
    }

}

class DiffCallback<T>(val old: List<T>, val new: List<T>,
                      val id: T.() -> Any,
                      val content: (T, T) -> Boolean = { o, n -> o == n },
                      val payload: (T, T) -> Any? = { _, _ -> null }) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition]!!.id() == new[newItemPosition]!!.id()
    }

    override fun getOldListSize(): Int = old.size

    override fun getNewListSize(): Int = new.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return content(old[oldItemPosition], new[newItemPosition])
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return payload(old[oldItemPosition], new[newItemPosition])
    }
}

fun RecyclerView.Adapter<*>.dispatchChanges(callback: DiffUtil.Callback) {
    DiffUtil.calculateDiff(callback).dispatchUpdatesTo(this)
}