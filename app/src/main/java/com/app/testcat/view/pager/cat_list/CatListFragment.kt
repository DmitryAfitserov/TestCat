package com.app.testcat.view.pager.cat_list

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.testcat.R
import com.app.testcat.databinding.FragmentCatListBinding
import com.app.testcat.extension.alert
import com.app.testcat.extension.gone
import com.app.testcat.extension.permissionDeniedAlert
import com.app.testcat.extension.show
import com.app.testcat.helper.DownloadHelper
import com.app.testcat.interfaces.CatItemListener
import com.app.testcat.model.CatUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CatListFragment : Fragment(), CatItemListener {

    private var _binding: FragmentCatListBinding? = null
    private val binding get() = _binding!!

    private var adapter: CatListAdapter? = null

    private val viewModel: CatListViewModel by viewModels()

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
        _binding = FragmentCatListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.update.setOnClickListener {
            adapter?.refresh()
        }

        val linearLayoutManager = LinearLayoutManager(context)
        binding.recycler.layoutManager = linearLayoutManager

        adapter = CatListAdapter()

        adapter?.setOnClickListener(this)

        binding.recycler.adapter = adapter?.withLoadStateFooter(StateAdapter {
            adapter?.retry()
        })

        lifecycleScope.launch {
            viewModel.paging().collectLatest {
                if (_binding == null) return@collectLatest
                adapter?.submitData(it)
            }
        }
        lifecycleScope.launch {
            viewModel.flowBd().collectLatest {
                adapter?.setListFavorite(it)
            }
        }
        lifecycleScope.launch {
            adapter?.loadStateFlow?.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        binding.update.gone()
                        binding.progressBar.show()
                    }
                    is LoadState.NotLoading -> {
                        binding.update.gone()
                        binding.progressBar.gone()
                    }
                    is LoadState.Error -> {
                        binding.update.show()
                        binding.progressBar.gone()
                    }
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        adapter?.notifyVisibleChangedItems()
    }


    override fun clickFavorite(item: CatUI) {

        viewModel.clickFavorite(item)

    }

    override fun clickImage(item: CatUI) {
        alert(getString(R.string.download_image), getString(R.string.download_image_description)) {
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