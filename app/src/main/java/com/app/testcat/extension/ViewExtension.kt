package com.app.testcat.extension

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.app.testcat.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder


fun Fragment.alert(title: String, message: String, callback: (() -> Unit)? = null) {
    if (this.isDetached) return
    MaterialAlertDialogBuilder(requireContext()).setTitle(title).setMessage(message)
        .setCancelable(false)
        .setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        .setPositiveButton(
            R.string.ok
        ) { _, _ ->
            callback?.invoke()
        }.show()
}


fun Fragment.permissionDeniedAlert(message: String) {
    val appSettingsIntent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", requireActivity().packageName, null)
    )

    MaterialAlertDialogBuilder(requireContext())
        .setTitle(getString(R.string.access_denied))
        .setMessage(message)
        .setPositiveButton(R.string.settings) { _, _ ->
            startActivity(appSettingsIntent)
        }
        .show()
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun ViewPager2.reduceDragSensitivity() {
    val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
    recyclerViewField.isAccessible = true
    val recyclerView = recyclerViewField.get(this) as RecyclerView

    val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
    touchSlopField.isAccessible = true
    val touchSlop = touchSlopField.get(recyclerView) as Int
    touchSlopField.set(recyclerView, touchSlop * 4)
}

