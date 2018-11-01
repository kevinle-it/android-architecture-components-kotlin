package net.snaglobal.trile.wizeye.customview

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.KeyEvent

/**
 * @author lmtri
 * @since Oct 08, 2018 at 4:39 PM
 */
class CustomEditText : AppCompatEditText {

    constructor(context: Context)
            : super(context)

    constructor(context: Context, attrs: AttributeSet)
            : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        event?.let {
            if (it.keyCode == KeyEvent.KEYCODE_BACK) {
                dispatchKeyEvent(it)
                this.clearFocus()
                return false
            }
        }
        return super.onKeyPreIme(keyCode, event)
    }
}