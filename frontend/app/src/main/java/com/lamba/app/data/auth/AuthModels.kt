package com.lamba.app.data.auth

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val email: String,
    val password: String,
    @SerializedName("full_name")
    val fullName: String?
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class TokenResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String
)

data class UserResponse(
    val id: Int,
    val email: String,
    @SerializedName("full_name")
    val fullName: String?,
    @SerializedName("created_at")
    val createdAt: String
)
