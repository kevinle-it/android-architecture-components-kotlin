package net.snaglobal.trile.wizeye.ui.fragment.map.list

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.snaglobal.trile.wizeye.R
import net.snaglobal.trile.wizeye.data.remote.model.MapListItem
import net.snaglobal.trile.wizeye.databinding.MapListItemBinding

/**
 * @author lmtri
 * @since Oct 28, 2018 at 9:04 PM
 */
class MapItemAdapter(private val mapItemClickListener: OnMapItemClickListener)
    : RecyclerView.Adapter<MapItemAdapter.MapItemViewHolder>() {

    private val mapList = ArrayList<MapListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapItemViewHolder {
        val binding: MapListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.map_list_item,
                parent,
                false
        )
        binding.clickListener = mapItemClickListener
        return MapItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MapItemViewHolder, position: Int) {
        holder.binding.mapListItem = mapList[position]
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return mapList.size
    }

    fun submitList(mapList: List<MapListItem>) {
        if (this.mapList.isEmpty()) {
            this.mapList.addAll(mapList)
            notifyItemRangeInserted(0, mapList.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return this@MapItemAdapter.mapList.size
                }

                override fun getNewListSize(): Int {
                    return mapList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return mapList[newItemPosition].displayName ==
                            this@MapItemAdapter.mapList[oldItemPosition].displayName
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return mapList[newItemPosition].displayName ==
                            this@MapItemAdapter.mapList[oldItemPosition].displayName
                }
            })
            this.mapList.clear()
            this.mapList.addAll(mapList)
            result.dispatchUpdatesTo(this)
        }
    }

    interface OnMapItemClickListener {
        fun onClick(mapItem: MapListItem)
    }

    class MapItemViewHolder(val binding: MapListItemBinding) : RecyclerView.ViewHolder(binding.root)
}