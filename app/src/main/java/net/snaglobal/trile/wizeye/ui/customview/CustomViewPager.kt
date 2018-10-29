package net.snaglobal.trile.wizeye.ui.customview

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * @author lmtri
 * @since Oct 24, 2018 at 3:37 PM
 */
class CustomViewPager @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null
) : ViewPager(context, attrs) {
    var isPagingEnabled: Boolean = false

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return if (isPagingEnabled) {
            super.onInterceptTouchEvent(ev)
        } else false
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return if (isPagingEnabled) {
            super.onTouchEvent(ev)
        } else false
    }
}