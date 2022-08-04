package com.pocs.presentation.extension

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pocs.presentation.R
import com.pocs.presentation.databinding.ContentLoadStateBinding
import java.net.ConnectException

fun <PA : PagingDataAdapter<T, VH>, T, VH> ContentLoadStateBinding.setListeners(
    adapter: PA,
    swipeToRefresh: SwipeRefreshLayout
) {
    swipeToRefresh.setOnRefreshListener { adapter.refresh() }

    this.retryButton.setOnClickListener {
        adapter.retry()
    }

    adapter.addLoadStateListener { loadStates ->
        val refreshLoadState = loadStates.refresh
        val isError = refreshLoadState is LoadState.Error
        val shouldShowEmptyText =
            refreshLoadState is LoadState.NotLoading && adapter.getItemCount() < 1

        if (refreshLoadState is LoadState.NotLoading) {
            swipeToRefresh.isRefreshing = false
        }
        emptyText.isVisible = shouldShowEmptyText
        progressBar.isVisible = refreshLoadState is LoadState.Loading
        retryButton.isVisible = isError
        errorMsg.isVisible = isError
        if (refreshLoadState is LoadState.Error) {
            errorMsg.text = when (val exception = refreshLoadState.error) {
                is ConnectException -> root.context.getString(R.string.fail_to_connect)
                else -> exception.message
            }
        }
    }
}