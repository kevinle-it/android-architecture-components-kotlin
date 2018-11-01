package net.snaglobal.trile.wizeye.ui.fragment.video.list

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.snaglobal.trile.wizeye.R
import net.snaglobal.trile.wizeye.data.remote.model.VideoListItem
import net.snaglobal.trile.wizeye.databinding.VideoListItemBinding

/**
 * @author lmtri
 * @since Oct 31, 2018 at 6:49 AM
 */
class VideoItemAdapter(private val videoItemClickListener: OnVideoItemClickListener)
    : RecyclerView.Adapter<VideoItemAdapter.VideoItemViewHolder>() {

    private val videoList = ArrayList<VideoListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoItemViewHolder {
        val binding: VideoListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.video_list_item,
                parent,
                false
        )
        binding.clickListener = videoItemClickListener
        return VideoItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoItemViewHolder, position: Int) {
        holder.binding.videoListItem = videoList[position]
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    fun submitList(videoList: List<VideoListItem>) {
        if (this.videoList.isEmpty()) {
            this.videoList.addAll(videoList)
            notifyItemRangeInserted(0, videoList.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return this@VideoItemAdapter.videoList.size
                }

                override fun getNewListSize(): Int {
                    return videoList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return videoList[newItemPosition].name ==
                            this@VideoItemAdapter.videoList[oldItemPosition].name
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return videoList[newItemPosition].name ==
                            this@VideoItemAdapter.videoList[oldItemPosition].name
                }
            })
            this.videoList.clear()
            this.videoList.addAll(videoList)
            result.dispatchUpdatesTo(this)
        }
    }

    interface OnVideoItemClickListener {
        fun onClick(videoItem: VideoListItem)
    }

    class VideoItemViewHolder(val binding: VideoListItemBinding) : RecyclerView.ViewHolder(binding.root)
}