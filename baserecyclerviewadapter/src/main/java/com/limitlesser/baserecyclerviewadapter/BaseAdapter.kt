package com.limitlesser.baserecyclerviewadapter

import android.support.v4.util.ArrayMap
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *  通用的recyclerView adapter 继承于ListAdapter 自动实现数据变化时的item动画
 *  无需重写onBindViewHolder等方法即可实现数据的绑定
 */
class BaseAdapter<D>(diffCallback: DiffUtil.ItemCallback<D>) : ListAdapter<D, RecyclerView.ViewHolder>(diffCallback) {

    private var types = mutableListOf<(D) -> Boolean>()
    private var createHolder = SparseArray<(ViewGroup, Int) -> RecyclerView.ViewHolder>(1)
    private var bindHolder = SparseArray<RecyclerView.ViewHolder.(D, Int) -> Unit>(1)
    private var itemClick = SparseArray<RecyclerView.ViewHolder.(D, Int) -> Unit>(1)

    /** 用于存放一些额外拓展数据 */
    val extras = ArrayMap<String, Any>()


    /**
     * 指定adapter的item与viewHolder
     * @param itemRes itemView对应layout资源
     * @param holder 创建viewHolder
     * @param onBind 绑定数据
     * @param onItemClick item点击处理
     * @param type 类型判断
     */
    @Suppress("UNCHECKED_CAST")
    fun <VH : RecyclerView.ViewHolder> item(itemRes: Int, holder: (View) -> VH,
                                            onBind: VH.(D, Int) -> Unit,
                                            onItemClick: (VH.(D, Int) -> Unit)? = null,
                                            type: (D) -> Boolean = { true }) {
        item({ parent -> LayoutInflater.from(parent.context).inflate(itemRes, parent, false) },
                holder, onBind, onItemClick, type)
    }

    /**
     * 指定adapter的item与viewHolder
     * @param itemView 创建itemView
     * @param holder 创建viewHolder
     * @param onBind 绑定数据
     * @param onItemClick item点击处理
     * @param type 类型判断
     */
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


/**
 *  创建一个BaseAdapter 默认自动设置为recyclerView的adapter
 *  @param diffCallback 处理数据变化的diffCallback
 *  @param attachToRecyclerView 是否设置为recyclerView的adapter
 */
inline fun <D> RecyclerView.baseAdapter(diffCallback: DiffUtil.ItemCallback<D> = DiffItemCallback(), attachToRecyclerView: Boolean = true,
                                        init: BaseAdapter<D>.() -> Unit): BaseAdapter<D> {
    val adapter = BaseAdapter(diffCallback)
    adapter.init()
    if (attachToRecyclerView)
        setAdapter(adapter)
    return adapter
}