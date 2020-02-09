package com.ivzb.arch.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.graphics.withTranslation
import androidx.core.view.forEach
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.State
import com.ivzb.arch.R
import com.ivzb.arch.util.isRtl
import kotlin.math.max
import kotlin.math.roundToInt

class HashtagIoDecoration(context: Context) : ItemDecoration() {

    private val drawable: Drawable?
    private val margin: Int

    private var decorBottom = 0

    init {
        val attrs = context.obtainStyledAttributes(
            R.style.HashtagIoDecoration,
            R.styleable.HashtagIoDecoration
        )
        drawable = attrs.getDrawable(R.styleable.HashtagIoDecoration_android_drawable)?.apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        }
        margin = attrs.getDimensionPixelSize(R.styleable.HashtagIoDecoration_margin, 0)
        attrs.recycle()

        decorBottom = 2 * margin + (drawable?.intrinsicHeight ?: 0)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
        // Decorate the last child only.
        if (drawable == null || parent.getChildAdapterPosition(view) != state.itemCount - 1) {
            super.getItemOffsets(outRect, view, parent, state)
        } else {
            outRect.set(0, 0, 0, decorBottom)
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: State) {
        if (drawable == null) {
            return
        }

        val x = if (parent.isRtl()) {
            parent.paddingEnd + margin
        } else {
            parent.width - parent.paddingEnd - margin - drawable.intrinsicWidth
        }

        val yFromParentBottom =
            parent.height - parent.paddingBottom - margin - drawable.intrinsicHeight
        if (state.itemCount < 1) {
            // No children. Just draw at the bottom of the parent.
            drawDecoration(canvas, x, yFromParentBottom)
            return
        }

        // Find the decorated view or bust.
        val child = findTargetChild(parent, state.itemCount - 1) ?: return
        val yFromChildBottom = child.bottom + child.translationY.roundToInt() + margin
        if (yFromChildBottom > parent.height) {
            // There's no room below the child, so the decoration is not visible.
            return
        }

        // Pin the decoration to the bottom of the parent if there's excess space.
        val y = max(yFromChildBottom, yFromParentBottom)
        drawDecoration(canvas, x, y)
    }

    private fun findTargetChild(parent: RecyclerView, adapterPosition: Int): View? {
        parent.forEach { child ->
            if (parent.getChildAdapterPosition(child) == adapterPosition) {
                return child
            }
        }
        return null
    }

    private fun drawDecoration(canvas: Canvas, x: Int, y: Int) {
        canvas.withTranslation(x = x.toFloat(), y = y.toFloat()) {
            drawable?.draw(canvas)
        }
    }
}
