package com.example.frutify.utils

import android.content.Context
import android.content.SharedPreferences

class SharePref(context: Context) {

    private var SHARED_PREF = "DATA_USER"
    private var sharedPref: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
    }

    fun setStringPreference(prefKey: String, value: String) {
        editor.putString(prefKey, value)
        editor.apply()
    }

    fun setIntPreference(prefKey: String, value: Int) {
        editor.putInt(prefKey, value)
        editor.apply()
    }

    fun setBooleanPreference(prefKey: String, value: Boolean) {
        editor.putBoolean(prefKey, value)
        editor.apply()
    }

    fun clearPreferenceByKey(prefKey: String) {
        editor.remove(prefKey)
        editor.apply()
    }

    fun clearPreferences() {
        editor.clear().apply()
    }

    val getToken: String
        get() = sharedPref.getString(Constant.PREF_TOKEN, "") ?: ""

    val getUserId: Int
        get() = sharedPref.getInt(Constant.PREF_USER_ID, 0)

    val isLogin: Boolean
        get() = sharedPref.getBoolean(Constant.PREF_IS_LOGIN, false)

    val getPhone: String
        get() = sharedPref.getString(Constant.PREF_USER_PHONE, "") ?: ""

    val getEmail: String
        get() = sharedPref.getString(Constant.PREF_EMAIL, "") ?: ""

    val getRoles: String
        get() = sharedPref.getString(Constant.ROLES, "") ?: ""

    val getUserRoles: String
        get() = sharedPref.getString(Constant.PREF_USER_ROLE, "") ?: ""

    val getFullname: String
        get() = sharedPref.getString(Constant.PREF_USER_FULLNAME, "") ?: ""

    val getAddress: String
        get() = sharedPref.getString(Constant.PREF_USER_ADDRESS, "") ?: ""

}