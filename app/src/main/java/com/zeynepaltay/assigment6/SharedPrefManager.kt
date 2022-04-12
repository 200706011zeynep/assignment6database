package com.zeynepaltay.assigment6

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences


class SharedPrefManager private constructor(private var ctx: Context) {
    //this method will store the user data in shared preferences
    fun userLogin(user: User) {
        val sharedPreferences: SharedPreferences = Companion.ctx.getSharedPreferences(
            SHARED_PREF_NAME, Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_ID, user.getId())
        editor.putString(KEY_USERNAME, user.getName())
        editor.putString(KEY_EMAIL, user.getEmail())
        editor.putString(KEY_GENDER, user.getGender())
        editor.apply()
    }

    //this method will checker whether user is already logged in or not
    val isLoggedIn: Boolean
        get() {
            val sharedPreferences: SharedPreferences = Companion.ctx.getSharedPreferences(
                SHARED_PREF_NAME, Context.MODE_PRIVATE
            )
            return sharedPreferences.getString(KEY_USERNAME, null) != null
        }

    //this method will give the logged in user
    val user: User
        get() {
            val sharedPreferences: SharedPreferences = Companion.ctx.getSharedPreferences(
                SHARED_PREF_NAME, Context.MODE_PRIVATE
            )
            return User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_GENDER, null)
            )
        }

    //this method will logout the user
    fun logout() {
        val sharedPreferences: SharedPreferences = Companion.ctx.getSharedPreferences(
            SHARED_PREF_NAME, Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        Companion.ctx.startActivity(
            Intent(
                Companion.ctx,
                LoginActivity::class.java
            )
        )
    }

    companion object {
        private const val SHARED_PREF_NAME = "volleyregisterlogin"
        private const val KEY_USERNAME = "keyusername"
        private const val KEY_EMAIL = "keyemail"
        private const val KEY_GENDER = "keygender"
        private const val KEY_ID = "keyid"
        private var mInstance: SharedPrefManager? = null
        @Synchronized
        fun getInstance(context: Context): SharedPrefManager? {
            if (mInstance == null) {
                mInstance = SharedPrefManager(context)
            }
            return mInstance
        }
    }
}
