package com.gokul.sample.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject

object Helper {

    fun <K, V> Map<K, V>.formatPostData(): RequestBody {
        val gson = Gson()
        val json = gson.toJson(this)
        return json.toString().toRequestBody("application/json".toMediaTypeOrNull())
    }

    fun String.toJsonObject(): JSONObject {
        return JSONObject(this)
    }

    fun ResponseBody?.getErrorMessage(): String{
        val errorResponse = this?.string()
        var message = "Something went wrong"
        try {
            val jsonData = errorResponse?.toJsonObject()
            if (jsonData?.has("message") == true) {
                jsonData.let { jsData->
                    message = jsData.getString("message")
                }
            }
        } catch (e: Exception) {
            Log.e("PARSE ERROR", e.message.toString())
            message = "Something went wrong"
        }
        return message
    }


    fun String.parseToModel(model: Class<*>): Any?{
        var result: Any? = null
        val gson = Gson()
        result = gson.fromJson<Any>(this, model)
        return result
    }


    fun AppCompatActivity.toast(message:String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }

    fun FragmentActivity.toast(message:String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }

    fun View.hideKeyboard() {
        if (this.getActivity()?.isKeyboardVisible() == true) {
            val imm : InputMethodManager = this.getActivity()?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.applicationWindowToken,0)
        }
    }

    private const val KEYBOARD_MIN_HEIGHT_RATIO = 0.15

    private fun Activity.isKeyboardVisible():Boolean{
        val r = Rect()

        val activityRoot = getActivityRoot(this)

        activityRoot.getWindowVisibleDisplayFrame(r)

        val location = IntArray(2)
        getContentRoot(this).getLocationOnScreen(location)

        val screenHeight = activityRoot.rootView.height
        val heightDiff = screenHeight - r.height() - location[1]

        return heightDiff > screenHeight * KEYBOARD_MIN_HEIGHT_RATIO
    }

    private fun View.getActivity(): Activity? {
        var context = context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }

    private fun getActivityRoot(activity: Activity): View {
        return getContentRoot(activity).rootView
    }

    private fun getContentRoot(activity: Activity): ViewGroup {
        return activity.findViewById(android.R.id.content)
    }
}