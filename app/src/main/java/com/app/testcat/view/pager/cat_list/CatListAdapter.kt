package com.app.testcat.view.pager.cat_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.testcat.R
import com.app.testcat.databinding.ItemCatBinding
import com.app.testcat.interfaces.CatItemListener
import com.app.testcat.model.CatNet
import com.app.testcat.model.CatUI
import com.bumptech.glide.Glide

class CatListAdapter : PagingDataAdapter<CatUI, CatListAdapter.CatHolder>(DiffCallback) {


    private var clickListener: CatItemListener? = null
    private var listFavorites: List<CatNet> = arrayListOf()

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCatBinding.inflate(layoutInflater, parent, false)
        return CatHolder(binding)
    }

    fun setOnClickListener(clickListener: CatItemListener) {
        this.clickListener = clickListener
    }

    fun setListFavorite(listFavorites: List<CatNet>) {
        this.listFavorites = listFavorites
    }

    fun notifyVisibleChangedItems() {
        snapshot().forEachIndexed { index, catUI ->
            catUI?.let {
                if (catUI.isFavorite) {
                    if (!listFavorites.any { it.id == catUI.id }) {
                        notifyItemChanged(index)
                    }
                }
            }
        }
    }


    override fun onBindViewHolder(holder: CatHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)

    }

    inner class CatHolder(private val binding: ItemCatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.buttonFavorite.setOnClickListener {
                snapshot()[layoutPosition]?.let {
                    clickListener?.clickFavorite(it)
                    setFavoriteState(it.isFavorite)
                }
            }

            binding.image.setOnClickListener {
                snapshot()[layoutPosition]?.let {
                    clickListener?.clickImage(it)
                }
            }
        }

        fun bind(item: CatUI) {
            Glide.with(binding.root.context)
                .load(item.url)
                .into(binding.image)

            item.isFavorite = listFavorites.any { catNet -> catNet.id == item.id }
            setFavoriteState(item.isFavorite)

        }

        private fun setFavoriteState(isFavorite: Boolean) {
            if (isFavorite) {
                binding.buttonFavorite.setImageResource(R.drawable.ic_favorite)
            } else {
                binding.buttonFavorite.setImageResource(R.drawable.ic_favorite_border)
            }
        }

    }
}