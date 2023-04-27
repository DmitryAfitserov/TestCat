package com.app.testcat.view.pager.cat_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.testcat.databinding.ItemCatStateBinding


class StateAdapter(private val listener: () -> (Unit)): LoadStateAdapter<StateAdapter.CatStateHolder>() {


    override fun onBindViewHolder(holder: CatStateHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): CatStateHolder {
        val binding = ItemCatStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatStateHolder(binding)
    }

    inner class CatStateHolder(private val binding: ItemCatStateBinding):
        RecyclerView.ViewHolder(binding.root){

        private var isClicker = false

        init {
            binding.root.setOnClickListener {
                if(isClicker)  listener.invoke()
            }
        }

        fun bind(loadState: LoadState){
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.update.isVisible = loadState !is LoadState.Loading
            isClicker = loadState !is LoadState.Loading
        }

    }

}