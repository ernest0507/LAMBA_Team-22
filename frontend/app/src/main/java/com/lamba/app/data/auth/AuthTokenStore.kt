package com.lamba.app.data.auth

import android.content.Context

class AuthTokenStore(context: Context) {
    private val preferences = context.applicationContext.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    fun getAccessToken(): String? {
        return preferences.getString(KEY_ACCESS_TOKEN, null)?.takeIf { it.isNotBlank() }
    }

    fun getUser(): UserResponse? {
        val id = preferences.getInt(KEY_USER_ID, USER_ID_NONE)
        val email = preferences.getString(KEY_USER_EMAIL, null)?.takeIf { it.isNotBlank() }
        val createdAt = preferences.getString(KEY_USER_CREATED_AT, null)?.takeIf { it.isNotBlank() }

        if (id == USER_ID_NONE || email == null || createdAt == null) {
            return null
        }

        return UserResponse(
            id = id,
            email = email,
            fullName = preferences.getString(KEY_USER_FULL_NAME, null),
            createdAt = createdAt
        )
    }

    fun saveSession(accessToken: String, user: UserResponse) {
        preferences.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .putInt(KEY_USER_ID, user.id)
            .putString(KEY_USER_EMAIL, user.email)
            .putString(KEY_USER_FULL_NAME, user.fullName)
            .putString(KEY_USER_CREATED_AT, user.createdAt)
            .apply()
    }

    fun clearSession() {
        preferences.edit()
            .clear()
            .apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "auth_session"
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_USER_ID = "user_id"
        const val KEY_USER_EMAIL = "user_email"
        const val KEY_USER_FULL_NAME = "user_full_name"
        const val KEY_USER_CREATED_AT = "user_created_at"
        const val USER_ID_NONE = -1
    }
}
