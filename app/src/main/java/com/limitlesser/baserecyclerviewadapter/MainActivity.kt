package com.limitlesser.baserecyclerviewadapter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.limitlesser.baserecyclerviewadapter.app.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.HORIZONTAL))
            val adapter = baseAdapter<String> {
                item(R.layout.list_item,
                        holder = {
                            object : RecyclerView.ViewHolder(it) {
                                val text = itemView.text
                            }
                        },
                        onBind = { data, _ ->
                            text.text = data
                        },
                        onItemClick = { data, _ -> Toast.makeText(this@MainActivity, data, Toast.LENGTH_SHORT).show() })
            }
            adapter.submitList(listOf("haushdfa", "asdfsa", "sdfsfd", "sdfs", "sdfsf"))
        }
    }
}

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    val data = mutableListOf<String>()

    fun setData(data: List<String>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = data[position]

        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, data[position], Toast.LENGTH_SHORT).show()
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text = itemView.text
    }
}
