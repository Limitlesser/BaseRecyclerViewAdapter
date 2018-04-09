package com.limitlesser.baserecyclerviewadapter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
