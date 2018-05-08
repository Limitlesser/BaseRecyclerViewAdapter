# BaseRecyclerViewAdapter

    更优雅的创建recyclerView adapter

recyclerView在Android开发中几乎是必不可少的展示列表的控件，而使用recyclerView必须要定义adapter，一个最基本的adapter往往是这样的：

```Java
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
```
可以看到上面那么多代码大部分是模板代码，真正关乎业务逻辑的部分很少。而一个adapter最主要的作用就是将数据与RecyclerView绑定，我们关心的东西应该是这样的：
```Java
val adapter = baseAdapter<String> {
                item(R.layout.list_item,
                        holder = ::ViewHolder,
                        onBind = { data, _ ->
                            itemView.text.text = data
                        },
                        onItemClick = { data, _ -> Toast.makeText(this@MainActivity, data, Toast.LENGTH_SHORT).show() })
            }
            adapter.submitList(listOf("haushdfa", "asdfsa", "sdfsfd", "sdfs", "sdfsf"))
        
```
