package net.snaglobal.trile.wizeye.customview

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.AutoCompleteTextView

/**
 * @author lmtri
 * @since Oct 08, 2018 at 4:15 PM
 */
class CustomAutoCompleteTextView : AutoCompleteTextView {

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