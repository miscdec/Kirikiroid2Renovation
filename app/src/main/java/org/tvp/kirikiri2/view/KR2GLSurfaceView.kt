package org.tvp.kirikiri2.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import org.cocos2dx.lib.Cocos2dxGLSurfaceView
import org.tvp.kirikiri2.KR2Activity

class KR2GLSurfaceView : Cocos2dxGLSurfaceView {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    override fun insertText(pText: String) {

        KR2Activity.nativeInsertText(pText)
    }

    override fun deleteBackward() {
        KR2Activity.nativeDeleteBackward()
    }

    override fun onKeyDown(pKeyCode: Int, pKeyEvent: KeyEvent): Boolean {
        return when (pKeyCode) {
            KeyEvent.KEYCODE_BACK, KeyEvent.KEYCODE_MENU, KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, KeyEvent.KEYCODE_DPAD_CENTER -> {
                KR2Activity.nativeKeyAction(pKeyCode, true)
                true
            }
            else -> super.onKeyDown(pKeyCode, pKeyEvent)
        }
    }

    override fun onKeyUp(pKeyCode: Int, pKeyEvent: KeyEvent): Boolean {
        return when (pKeyCode) {
            KeyEvent.KEYCODE_BACK, KeyEvent.KEYCODE_MENU, KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, KeyEvent.KEYCODE_DPAD_CENTER -> {
                KR2Activity.nativeKeyAction(pKeyCode, false)
                true
            }
            else -> super.onKeyUp(pKeyCode, pKeyEvent)
        }
    }

    override fun onHoverEvent(pMotionEvent: MotionEvent): Boolean {
        val pointerNumber = pMotionEvent.pointerCount
        val xs = FloatArray(pointerNumber)
        val ys = FloatArray(pointerNumber)
        for (i in 0 until pointerNumber) {
            xs[i] = pMotionEvent.getX(i)
            ys[i] = pMotionEvent.getY(i)
        }
        when (pMotionEvent.actionMasked) {
            MotionEvent.ACTION_HOVER_MOVE -> KR2Activity.nativeHoverMoved(
                xs[0], ys[0]
            )
        }
        return true
    }

    override fun onTouchEvent(pMotionEvent: MotionEvent): Boolean {

        // these data are used in ACTION_MOVE and ACTION_CANCEL
        val pointerNumber = pMotionEvent.pointerCount
        val ids = IntArray(pointerNumber)
        val xs = FloatArray(pointerNumber)
        val ys = FloatArray(pointerNumber)
        for (i in 0 until pointerNumber) {
            ids[i] = pMotionEvent.getPointerId(i)
            xs[i] = pMotionEvent.getX(i)
            ys[i] = pMotionEvent.getY(i)
        }
        when (pMotionEvent.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                val indexPointerDown =
                    pMotionEvent.action shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
                val idPointerDown = pMotionEvent.getPointerId(indexPointerDown)
                val xPointerDown = pMotionEvent.getX(indexPointerDown)
                val yPointerDown = pMotionEvent.getY(indexPointerDown)
                KR2Activity.nativeTouchesBegin(idPointerDown, xPointerDown, yPointerDown)
            }
            MotionEvent.ACTION_DOWN -> {
                // there are only one finger on the screen
                val idDown = pMotionEvent.getPointerId(0)
                val xDown = xs[0]
                val yDown = ys[0]
                KR2Activity.nativeTouchesBegin(idDown, xDown, yDown)
            }
            MotionEvent.ACTION_MOVE -> KR2Activity.nativeTouchesMove(ids, xs, ys)
            MotionEvent.ACTION_POINTER_UP -> {
                val indexPointUp =
                    pMotionEvent.action shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
                val idPointerUp = pMotionEvent.getPointerId(indexPointUp)
                val xPointerUp = pMotionEvent.getX(indexPointUp)
                val yPointerUp = pMotionEvent.getY(indexPointUp)
                KR2Activity.nativeTouchesEnd(idPointerUp, xPointerUp, yPointerUp)
            }
            MotionEvent.ACTION_UP -> {
                // there are only one finger on the screen
                val idUp = pMotionEvent.getPointerId(0)
                val xUp = xs[0]
                val yUp = ys[0]
                KR2Activity.nativeTouchesEnd(idUp, xUp, yUp)
            }
            MotionEvent.ACTION_CANCEL -> KR2Activity.nativeTouchesCancel(ids, xs, ys)
        }

        /*
        if (BuildConfig.DEBUG) {
            Cocos2dxGLSurfaceView.dumpMotionEvent(pMotionEvent);
        }
        */
        return true
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_SCROLL -> {
                val v = event.getAxisValue(MotionEvent.AXIS_VSCROLL)
                KR2Activity.nativeMouseScrolled(-v)
                return true
            }
            else -> {}
        }
        return super.onGenericMotionEvent(event)
    }
}