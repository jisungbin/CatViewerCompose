package io.github.jisungbin.catviewercompose.paging

import androidx.recyclerview.widget.DiffUtil

object CatComparator : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String) =
        oldItem.hashCode() == newItem.hashCode()

    override fun areContentsTheSame(oldItem: String, newItem: String) =
        oldItem == newItem
}
