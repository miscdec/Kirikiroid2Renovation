package org.tvp.kirikiri2.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import org.cocos2dx.lib.Cocos2dxActivity
import org.tvp.kirikiri2.KR2Activity

class DialogMessage {
//    var Title: String? = null
//    var Text: String? = null
//    lateinit var Buttons: Array<String?>
//    private var TextEditor: EditText? = null
//    fun Init(title: String?, text: String?, buttons: Array<String?>) {
//        Title = title
//        Text = text
//        Buttons = buttons
//    }
//
//    fun onButtonClick(n: Int) {
//        if (TextEditor != null) {
//            KR2Activity.onMessageBoxText(TextEditor!!.text.toString())
//        }
//        KR2Activity.onMessageBoxOK(n)
//    }
//
//    fun CreateBuilder(): AlertDialog.Builder {
//        /*	TextView showText = new TextView(sInstance);
//        showText.setText(Text);
//        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
//            showText.setTextIsSelectable(true);*/
//        var builder = AlertDialog.Builder(KR2Activity.sInstance).setTitle(Title)
//            .setMessage(Text)
//            .setCancelable(true) //setView(showText).
//        false
//        if (Buttons.size >= 1) {
//            builder =
//                builder.setPositiveButton(Buttons[0]) { dialog: DialogInterface?, which: Int ->
//                    onButtonClick(0)
//                }
//        }
//        if (Buttons.size >= 2) {
//            builder =
//                builder.setNeutralButton(Buttons[1]) { dialog: DialogInterface?, which: Int ->
//                    onButtonClick(1)
//                }
//        }
//        if (Buttons.size >= 3) {
//            builder =
//                builder.setNegativeButton(Buttons[2]) { dialog: DialogInterface?, which: Int ->
//                    onButtonClick(2)
//                }
//        }
//        return builder
//    }
//
//    fun ShowMessageBox() {
//        CreateBuilder().create().show()
//    }
//
//    fun ShowInputBox(text: String?) {
//        val builder = CreateBuilder()
//        TextEditor = EditText(KR2Activity.sInstance)
//        val lp = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.MATCH_PARENT
//        )
//        TextEditor!!.layoutParams = lp
//        TextEditor!!.setText(text)
//        builder.setView(TextEditor)
//        val ad = builder.create()
//        ad.show()
//        TextEditor!!.requestFocus()
//        val imm =
//            Cocos2dxActivity.getContext()
//                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.showSoftInput(TextEditor, 0)
//    }
}