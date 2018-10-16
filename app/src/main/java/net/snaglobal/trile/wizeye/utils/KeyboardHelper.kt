package net.snaglobal.trile.wizeye.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * @author lmtri
 * @since Oct 15, 2018 at 1:43 PM
 */
object KeyboardHelper {
    @JvmStatic
    fun showSoftKeyboard(activity: Activity?, editText: EditText) {
        activity?.run {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    @JvmStatic
    fun hideSoftKeyboard(activity: Activity?, clearCurrentFocus: Boolean) {
        activity?.run {
            // Check if any view has focus.
            val view = currentFocus
            view?.let {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                if (clearCurrentFocus) {
                    it.clearFocus()
                }
            }
        }
    }
}