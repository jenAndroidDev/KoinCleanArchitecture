package com.example.koincleanarchitecture.utils.extension

import android.content.Context
import android.hardware.input.InputManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText


fun AppCompatEditText.actionDone(callback:()->Unit){

    setOnEditorActionListener { v, actionId, _ ->
        if (actionId==EditorInfo.IME_ACTION_DONE){
            callback.invoke()
            val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken,0)
            return@setOnEditorActionListener true
        }
        false
    }

}
fun AppCompatEditText.actionNext(callback: () -> Unit){

    setOnEditorActionListener { _, actionId, _ ->

        if (actionId==EditorInfo.IME_ACTION_NEXT){
            callback.invoke()
            return@setOnEditorActionListener true
        }

        false
    }
}
