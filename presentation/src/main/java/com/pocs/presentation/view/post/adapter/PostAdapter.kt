package com.pocs.presentation.view.post.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.pocs.presentation.databinding.ItemPostBinding
import com.pocs.presentation.model.post.item.PostItemUiState
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.utils.NoCopySpannableFactory

class PostAdapter(
    private val onClickItem: (PostItemUiState) -> Unit
) : PagingDataAdapter<PostItemUiState, PostViewHolder>(diffCallback) {

    private var markwon: Markwon? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        if (markwon == null) {
            markwon = Markwon.builder(parent.context).usePlugin(object : AbstractMarkwonPlugin() {
                override fun afterSetText(textView: TextView) {
                    // 마크다운으로 꾸며진 스타일을 제거하기 위해 span을 문자열로 전환하여 넣는다.
                    val text = textView.text.toString()
                    textView.text = text
                }
            }).build()
        }
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPostBinding.inflate(layoutInflater, parent, false)
        // 최적화를 하기 위해 아래와 같이 정적 팩토리를 사용한다. 이는 마크다운 제거를 위해 사용된다.
        binding.content.setSpannableFactory(NoCopySpannableFactory.getInstance())
        return PostViewHolder(binding, onClickItem, markwon!!)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<PostItemUiState>() {
            override fun areItemsTheSame(
                oldItem: PostItemUiState,
                newItem: PostItemUiState
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PostItemUiState,
                newItem: PostItemUiState
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}