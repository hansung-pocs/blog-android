package com.pocs.presentation.extension

import android.graphics.drawable.InsetDrawable
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

fun RecyclerView.addDividerDecoration(@DimenRes horizontalPaddingDimen: Int? = null) {
    val attr = intArrayOf(android.R.attr.listDivider)
    val typeArray = context.obtainStyledAttributes(attr)
    val divider = typeArray.getDrawable(0)
    typeArray.recycle()
    val inset = horizontalPaddingDimen?.let { resources.getDimensionPixelSize(it) } ?: 0
    val insetDivider = InsetDrawable(divider, inset, 0, inset, 0)
    val decoration = DividerItemDecoration(context, LinearLayout.VERTICAL)
    decoration.setDrawable(insetDivider)
    addItemDecoration(decoration)
}

fun TextView.syncLineHeight(figmaLineHeight: Float, factor: Float = 1.48f) {
    val lineSpacingExtra = figmaLineHeight.toDp() - factor * this.textSize

    val padding = lineSpacingExtra
        .takeIf { it != 0.0f }
        ?.div(2)
        ?.roundToInt()
        ?: 0

    setLineSpacing(lineSpacingExtra, 1f)
    updatePadding(
        top = padding,
        bottom = padding
    )
}
