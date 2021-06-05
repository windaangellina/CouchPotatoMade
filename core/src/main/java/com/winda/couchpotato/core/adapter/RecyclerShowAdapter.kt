package com.winda.couchpotato.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.winda.couchpotato.core.R
import com.winda.couchpotato.core.databinding.ItemShowsCardBinding
import com.winda.couchpotato.core.domain.model.Show

class RecyclerShowAdapter(private var listShow : ArrayList<Show>) : RecyclerView.Adapter<RecyclerShowAdapter.ViewHolder>() {
    private lateinit var binding : ItemShowsCardBinding

    // buat onItemClick
    private var onItemClickCallback : OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback : OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        binding = ItemShowsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listShow[position])
    }

    override fun getItemCount(): Int {
        return listShow.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(binding: ItemShowsCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(showEntity: Show){
            binding.tvTitle.text = showEntity.getTitleWithReleaseYear()
            binding.tvOverview.text = showEntity.overview

            // glide poster
            if (showEntity.posterUrl != null){
                Glide.with(itemView.context)
                    .load(showEntity.posterUrl)
                    .placeholder(R.drawable.app_icon_bg)
                    .error(R.drawable.app_icon_bg) // will be displayed if the image cannot be loaded
                    .dontAnimate()
                    .into(binding.imgPoster)
            }


            // glide backdrop
            if (showEntity.backdropUrl != null){
                Glide.with(itemView.context)
                    .load(showEntity.backdropUrl)
                    .placeholder(R.drawable.app_icon_bg)
                    .error(R.drawable.gradient_main) // will be displayed if the image cannot be loaded
                    .dontAnimate()
                    .into(binding.imgBackdrop)
            }


            // on click
            itemView.setOnClickListener {
                onItemClickCallback?.onItemClickCallback(showEntity)
            }
        }
    }

    interface OnItemClickCallback{
        fun onItemClickCallback(show : Show)
    }
}