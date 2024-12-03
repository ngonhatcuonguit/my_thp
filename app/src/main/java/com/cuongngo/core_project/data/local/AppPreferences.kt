package com.cuongngo.core_project.data.local

import android.content.Context
import android.content.SharedPreferences
import com.cuongngo.core_project.App
import com.cuongngo.core_project.model.DeviceInfo
import com.cuongngo.core_project.response.login_response.LoginResponse
import com.cuongngo.core_project.ui.event_thp.model.Examiner
import com.google.gson.GsonBuilder

object AppPreferences {

    var preferences: SharedPreferences
    private var editor: SharedPreferences.Editor
    private const val REFERENCES_NAME = "AppPreferences"
    private const val KEY_SHOWN_ON_BOARD = "key_shown_on_board"
    const val KEY_USER_ACCESS_TOKEN = "USER_ACCESS_TOKEN"
    const val KEY_COUNT_RECORD_LOCAL_USER = "KEY_COUNT_RECORD_LOCAL_USER"
    const val KEY_COUNT_RECORD_LOCAL_FORM = "KEY_COUNT_RECORD_LOCAL_FORM"
    const val KEY_USER_INFO = "KEY_USER_INFO"
    const val KEY_GK_INFO = "KEY_GK_INFO"
    const val KEY_DEVICE_INFO = "KEY_DEVICE_INFO"
    const val KEY_NICK_NAME = "KEY_NICK_NAME"
    const val KEY_NAME = "KEY_NAME"
    const val KEY_HO_VA_TEN = "KEY_HO_VA_TEN"
    const val KEY_SD_KHA_DUNG = "KEY_SO_DU_KHA_DUNG"
    const val KEY_SD_THUC = "KEY_SD_THUC"
    const val KEY_TONG_SD = "KEY_TONG_SD"
    const val KEY_DIEM = "KEY_DIEM"
    const val KEY_STK = "KEY_STK"
    const val KEY_THE = "KEY_THE"

    init{
        preferences = App.getInstance().getSharedPreferences(REFERENCES_NAME, Context.MODE_PRIVATE)
        editor = preferences.edit()
    }

    fun getACBInfo(key: String): String {
        return preferences.getString(key, "") ?: ""
    }
    fun setACBInfo(key: String,value: String) {
        editor.also {
            it.putString(key, value)
            it.commit()
        }
    }

    fun saveGKInfo(user: Examiner?){
        editor.putObject(user, KEY_GK_INFO)
    }

    fun getGKInfo(): Examiner? {
        return preferences.getObject(KEY_GK_INFO)
    }

    fun getUserAccessToken(): String {
        return preferences.getString(KEY_USER_ACCESS_TOKEN, "") ?: ""
    }

    fun setUserAccessToken(token: String) {
        editor.also {
            it.putString(KEY_USER_ACCESS_TOKEN, token)
            it.commit()
        }
    }

    fun getCountRecordLocalUser(): Int {
        return preferences.getInt(KEY_COUNT_RECORD_LOCAL_USER, 0) ?: 0
    }

    fun setCountRecordLocalUser(count: Int) {
        editor.also {
            it.putInt(KEY_COUNT_RECORD_LOCAL_USER, count)
            it.commit()
        }
    }
    fun getCountRecordLocalForm(): Int {
        return preferences.getInt(KEY_COUNT_RECORD_LOCAL_FORM, 0) ?: 0
    }

    fun setCountRecordLocalForm(count: Int) {
        editor.also {
            it.putInt(KEY_COUNT_RECORD_LOCAL_FORM, count)
            it.commit()
        }
    }

    fun saveUserInfo(user: LoginResponse?){
        editor.putObject(user, KEY_USER_INFO)
    }

    fun getUserInfo(): LoginResponse? {
        return preferences.getObject(KEY_USER_INFO)
    }
    fun saveDeviceInfo(deviceInfo: DeviceInfo?) {
        editor.putObject(deviceInfo, KEY_DEVICE_INFO)
    }

    fun getDeviceInfo(): DeviceInfo? {
        return preferences.getObject(KEY_DEVICE_INFO)
    }

    /**
     * set shown onboard
     */
    fun setShowOnBoard(isShown: Boolean){
        editor.also {
            it.putBoolean(KEY_SHOWN_ON_BOARD, isShown)
            it.commit()
        }
    }

    /**
     * is user already view onboard screen or not
     */
    fun isShownOnBoard():Boolean{
        return preferences.getBoolean(KEY_SHOWN_ON_BOARD,false)
    }


    /**
     * Saves object into the Preferences.
     *
     * @param `object` Object of model class (of type [T]) to save
     * @param key Key with which Shared preferences to
     **/
    fun SharedPreferences.Editor.putObject(`object`: Any?, key: String) {
        //Convert object to JSON String.
        try {
            val jsonString = GsonBuilder().create().toJson(`object`)
            //Save that String in SharedPreferences
            putString(key, jsonString).apply()
        }catch (e:Throwable){}
    }
    /**
     * Used to retrieve object from the Preferences.
     *
     * @param key Shared Preference key with which object was saved.
     **/
    inline fun <reified T> SharedPreferences.getObject(key: String): T? {
        //We read JSON String which was saved.
        val value = getString(key, null)
        //JSON String was found which means object can be read.
        //We convert this JSON String to model object. Parameter "c" (of
        //type Class < T >" is used to cast.
        return try {
            GsonBuilder().create().fromJson(value, T::class.java)
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
    }

}