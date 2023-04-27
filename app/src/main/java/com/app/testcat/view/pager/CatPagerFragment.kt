package com.app.testcat.view.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.testcat.R
import com.app.testcat.databinding.FragmentPagerBinding
import com.app.testcat.extension.reduceDragSensitivity
import com.app.testcat.view.pager.cat_list.CatListFragment
import com.app.testcat.view.pager.favorite_list.FavoriteListFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatPagerFragment: Fragment() {

    private var _binding: FragmentPagerBinding? = null
    private val binding get() = _binding!!

    private var adapter: CatPagerAdapter? = null

    private val fragments: List<Fragment> by lazy {
        listOf(CatListFragment(), FavoriteListFragment())
    }

    private val titles by lazy {
        arrayOf(getString(R.string.cats), getString(R.string.favorites))
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CatPagerAdapter(this, fragments)

        binding.pager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, i ->
            tab.text = titles[i]
        }.attach()

        binding.pager.reduceDragSensitivity()

    }

}