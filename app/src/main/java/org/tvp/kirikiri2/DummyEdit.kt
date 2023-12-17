package org.tvp.kirikiri2

import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import kotlin.jvm.internal.Intrinsics


class DummyEdit(context: Context?) : View(context), View.OnKeyListener {
    var ic: InputConnection? = null
    override fun onCheckIsTextEditor(): Boolean {
        return true
    }

    override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
        Intrinsics.checkNotNullParameter(v, "v")
        Intrinsics.checkNotNullParameter(event, "event")
        return if (event.isPrintingKey) {
            if (event.action == 0) {
                val var10000 = ic
                Intrinsics.checkNotNull(var10000)
                var10000!!.commitText(event.unicodeChar.toChar().toString() as CharSequence, 1)
            }
            true
        } else {
            false
        }
    }

    //
    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        // As seen on StackOverflow: http://stackoverflow.com/questions/7634346/keyboard-hide-event
        // FIXME: Discussion at http://bugzilla.libsdl.org/show_bug.cgi?id=1639
        // FIXME: This is not a 100% effective solution to the problem of detecting if the keyboard is showing or not
        // FIXME: A more effective solution would be to change our Layout from AbsoluteLayout to Relative or Linear
        // FIXME: And determine the keyboard presence doing this: http://stackoverflow.com/questions/2150078/how-to-check-visibility-of-software-keyboard-in-android
        // FIXME: An even more effective way would be if Android provided this out of the box, but where would the fun be in that :)
        if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
            if (KR2Activity.mTextEdit != null && KR2Activity.mTextEdit.visibility == VISIBLE) {
                KR2Activity.hideTextInput()
                //KR2Activity.nativeKeyboardFocusLost();
            }
        }
        return super.onKeyPreIme(keyCode, event)
    }


    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection {
        Intrinsics.checkNotNullParameter(outAttrs, "outAttrs")
        ic = SDLInputConnection(this as View, true)
        outAttrs.imeOptions = 301989888
        val var10000 = ic
        return if (var10000 == null) {
            throw NullPointerException("null cannot be cast to non-null type org.tvp.kirikiri2.SDLInputConnection")
        } else {
            var10000 as SDLInputConnection
        }
    }

    init {
        this.isFocusableInTouchMode = true
        this.isFocusable = true
        setOnKeyListener(this as OnKeyListener)
    }
}