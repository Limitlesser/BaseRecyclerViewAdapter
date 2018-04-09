package com.limitlesser.baserecyclerviewadapter

import android.support.v4.util.ArrayMap
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class BaseAdapter<D>(diffCallback: DiffUtil.ItemCallback<D>) : ListAdapter<D, RecyclerView.ViewHolder>(diffCallback) {

    private var types = mutableListOf<(D) -> Boolean>()
    private var createHolder = SparseArray<(ViewGroup, Int) -> RecyclerView.ViewHolder>(1)
    private var bindHolder = SparseArray<RecyclerView.ViewHolder.(D, Int) -> Unit>(1)
    private var itemClick = SparseArray<RecyclerView.ViewHolder.(D, Int) -> Unit>(1)

    /** 用于存放一些额外拓展数据 */
    val extras = ArrayMap<String, Any>()


    @Suppress("UNCHECKED_CAST")
    fun <VH : RecyclerView.ViewHolder> item(itemRes: Int, holder: (View) -> VH,
                                            onBind: VH.(D, Int) -> Unit,
                                            onItemClick: (VH.(D, Int) -> Unit)? = null,
                                            type: (D) -> Boolean = { true }) {
        item({ parent -> LayoutInflater.from(parent.context).inflate(itemRes, parent, false) },
                holder, onBind, onItemClick, type)
    }

    @Suppress("UNCHECKED_CAST")
    fun <VH : RecyclerView.ViewHolder> item(itemView: (ViewGroup) -> View,
                                            holder: (View) -> VH,
                                            onBind: VH.(D, Int) -> Unit,
                                            onItemClick: (VH.(D, Int) -> Unit)? = null,
                                            type: (D) -> Boolean = { true }) {
        types.add(type)
        val viewType = types.lastIndex
        createHolder[viewType] = { parent, _ ->
            holder(itemView(parent))
        }
        bindHolder[viewType] = onBind as (RecyclerView.ViewHolder.(D, Int) -> Unit)?
        itemClick[viewType] = onItemClick as (RecyclerView.ViewHolder.(D, Int) -> Unit)?
    }


    override fun getItemViewType(position: Int): Int {
        return types.indexOfFirst { it(getItem(position)) }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bindHolder[getItemViewType(position)].invoke(holder, getItem(position), position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return createHolder[viewType](parent, viewType).apply {
            itemClick[viewType]?.let { itemClick ->
                itemView.setOnClickListener { itemClick.invoke(this, getItem(adapterPosition), adapterPosition) }
            }
        }
    }


    operator fun <E> SparseArray<E>.set(key: Int, value: E?) {
        put(key, value)
    }
}


/** 创建一个BaseAdapter 默认自动设置为recyclerView的adapter */
inline fun <D> RecyclerView.baseAdapter(diffCallback: DiffUtil.ItemCallback<D> = DiffItemCallback(), attachToRecyclerView: Boolean = true,
                                        init: BaseAdapter<D>.() -> Unit): BaseAdapter<D> {
    val adapter = BaseAdapter(diffCallback)
    adapter.init()
    if (attachToRecyclerView)
        setAdapter(adapter)
    return adapter
}