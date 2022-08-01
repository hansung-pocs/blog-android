package com.pocs.presentation.extension

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import com.pocs.presentation.R
import com.pocs.presentation.databinding.ContentLoadStateBinding
import java.net.ConnectException

fun <PA : PagingDataAdapter<T, VH>, T, VH> ContentLoadStateBinding.setListeners(adapter: PA) {
    this.retryButton.setOnClickListener {
        adapter.retry()
    }

    adapter.addLoadStateListener { loadStates ->
        val refreshLoadState = loadStates.refresh
        val isError = loadStates.refresh is LoadState.Error

        this.progressBar.isVisible = loadStates.refresh is LoadState.Loading
        this.retryButton.isVisible = isError
        this.errorMsg.isVisible = isError
        if (refreshLoadState is LoadState.Error) {
            errorMsg.text = when (val exception = refreshLoadState.error) {
                is ConnectException -> root.context.getString(R.string.fail_to_connect)
                else -> exception.message
            }
        }
    }
}