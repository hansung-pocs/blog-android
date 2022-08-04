package com.pocs.presentation.extension

import android.graphics.drawable.InsetDrawable
import android.widget.LinearLayout
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addDividerDecoration(@DimenRes horizontalPaddingDimen: Int? = null) {
    val attr = intArrayOf(android.R.attr.listDivider)
    val typeArray = context.obtainStyledAttributes(attr)
    val divider = typeArray.getDrawable(0)
    val inset = horizontalPaddingDimen?.let { resources.getDimensionPixelSize(it) } ?: 0
    val insetDivider = InsetDrawable(divider, inset, 0, inset, 0)
    val decoration = DividerItemDecoration(context, LinearLayout.VERTICAL)
    decoration.setDrawable(insetDivider)
    addItemDecoration(decoration)
}