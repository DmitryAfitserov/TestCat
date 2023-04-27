package com.app.testcat.view.pager.favorite_list

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.testcat.R
import com.app.testcat.databinding.FragmentFavoriteListBinding
import com.app.testcat.extension.alert
import com.app.testcat.extension.gone
import com.app.testcat.extension.permissionDeniedAlert
import com.app.testcat.extension.show
import com.app.testcat.helper.DownloadHelper
import com.app.testcat.interfaces.CatItemListener
import com.app.testcat.model.CatUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteListFragment: Fragment(), CatItemListener {

    private var _binding: FragmentFavoriteListBinding? = null
    private val binding get() = _binding!!

    private var adapter: FavouriteListAdapter? = null

    private val viewModel: FavoriteListViewModel by viewModels()

    private var cashItem: CatUI? = null

    private val requestStoragePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ::onStoragePermissionResult
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(context)
        binding.recycler.layoutManager = linearLayoutManager

        adapter = FavouriteListAdapter()

        adapter?.setOnClickListener(this)

        binding.recycler.adapter = adapter


        lifecycle.coroutineScope.launch {
            viewModel.getCats().collect() {
                adapter?.submitList(it){
                    if(it.isEmpty()){
                        binding.emptyText.show()
                    } else {
                        binding.emptyText.gone()
                    }
                }
            }
        }

    }

    override fun clickFavorite(item: CatUI) {
        viewModel.deleteFavorite(item)
    }

    override fun clickImage(item: CatUI) {
        alert(getString(R.string.download_image), getString(R.string.download_image_description)){
            if (Build.VERSION.SDK_INT <= 28) {
                cashItem = item
                requestStoragePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                DownloadHelper.download(item.url, requireContext())
            }
        }
    }

    private fun onStoragePermissionResult(granted: Boolean) {
        if (granted) {
            cashItem?.let {
                DownloadHelper.download(it.url, requireContext())
            }
            cashItem = null

        } else {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                permissionDeniedAlert(getString(R.string.storage_permission_message))
            }
        }
    }
}