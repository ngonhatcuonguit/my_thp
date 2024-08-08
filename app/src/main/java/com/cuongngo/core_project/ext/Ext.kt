package com.cuongngo.core_project.ext
import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

inline fun <reified T, reified LD: LiveData<T>> Fragment.observeLiveDataChanged(liveData: LD,  crossinline onChanged: (T) -> Unit){
    liveData.observe(viewLifecycleOwner, onChanged)
}

inline fun <reified T, reified LD: LiveData<T>> AppCompatActivity.observeLiveDataChanged(liveData: LD, observer: Observer<in T>){
    liveData.observe(this, observer)
}

inline fun <reified T, reified LD: LiveData<T>> Application.observeLiveDataChangedForever(liveData: LD, observer: Observer<in T>){
    liveData.observeForever(observer)
}

fun String.toRequestBody(): RequestBody {
    return this.toRequestBody("multipart/from-data".toMediaTypeOrNull())
}

fun String.toMultipartBody(key: String): MultipartBody.Part {
    return MultipartBody.Part.createFormData(key, this)
}

fun View.hideKeyboard() {
    val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.showKeyBoard() {
    val imm =
        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun String.asRawRequestBody(): RequestBody {
    return this.toRequestBody("application/json".toMediaTypeOrNull())
}

fun <T: View> viewsOf(vararg  views: T, action: T.() -> Unit){
    views.forEach {
        it.action()
    }
}

fun <T> doOnEach(vararg  views: T, action: (T) -> Unit){
    views.forEach {
        action.invoke(it)
    }
}

inline fun <reified T> Any.cast(): T {
    return this as T
}

inline fun <reified T> Any?.nullableCast(): T? {
    return try {
        this as? T
    }
    catch (e: Exception){
        null
    }
}
inline fun <reified T> Any?.breakReferenceCast(): T? {
    return try {
        this?.toJson()?.fromJson<T>()
    }
    catch (e: Exception){
        null
    }
}



fun Any.toJson(): String{
    return try {
        Gson().toJson(this)
    } catch (ex: Exception){
        ex.message?:"Parse json failed on $this"
    }
}

inline fun <reified T> String.fromJson(): T {
    return Gson().fromJson(this, T::class.java)
}

inline fun <reified T> fromRawText(rawText: String?): T? {
    return try {
        val gson = Gson()
        gson.fromJson(rawText, T::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun WTF(msg: String?, tag: String = "WTF"): Int{
    return Log.e(tag, "$msg")
}