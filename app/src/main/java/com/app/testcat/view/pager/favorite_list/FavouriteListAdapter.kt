package com.app.testcat.view.pager.favorite_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.testcat.R
import com.app.testcat.databinding.ItemCatBinding
import com.app.testcat.interfaces.CatItemListener
import com.app.testcat.model.CatUI
import com.bumptech.glide.Glide


class FavouriteListAdapter: ListAdapter<CatUI, FavouriteListAdapter.CatHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<CatUI>() {
            override fun areItemsTheSame(oldItem: CatUI, newItem: CatUI): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CatUI, newItem: CatUI): Boolean {
                return oldItem == newItem
            }
        }
    }


    private var clickListener: CatItemListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCatBinding.inflate(layoutInflater, parent, false)
        return CatHolder(binding)
    }

    fun setOnClickListener(clickListener: CatItemListener) {
        this.clickListener = clickListener
    }


    override fun onBindViewHolder(holder: CatHolder, position: Int) {

        val item = getItem(position) ?: return

        holder.bind(item)

    }



    inner class CatHolder(private val binding: ItemCatBinding): RecyclerView.ViewHolder(binding.root){


        init {
            binding.buttonFavorite.setOnClickListener {
                getItem(layoutPosition)?.let {
                    clickListener?.clickFavorite(it)
                }
            }

            binding.image.setOnClickListener {
                getItem(layoutPosition)?.let {
                    clickListener?.clickImage(it)
                }
            }

        }

        fun bind(item: CatUI){

            Glide.with(binding.root.context)
                .load(item.url)
                .into(binding.image)

            if(item.isFavorite){
                binding.buttonFavorite.setImageResource(R.drawable.ic_favorite)
            } else {
                binding.buttonFavorite.setImageResource(R.drawable.ic_favorite_border)
            }

        }

    }
}