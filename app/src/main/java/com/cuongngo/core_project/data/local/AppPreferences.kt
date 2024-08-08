package com.cuongngo.core_project.data.local

import android.content.Context
import android.content.SharedPreferences
import com.cuongngo.core_project.App

object AppPreferences {

    var preferences: SharedPreferences
    private var editor: SharedPreferences.Editor
    private const val REFERENCES_NAME = "AppPreferences"
    private const val KEY_SHOWN_ON_BOARD = "key_shown_on_board"
    const val KEY_USER_ACCESS_TOKEN = "USER_ACCESS_TOKEN"

    init{
        preferences = App.getInstance().getSharedPreferences(REFERENCES_NAME, Context.MODE_PRIVATE)
        editor = preferences.edit()
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

}