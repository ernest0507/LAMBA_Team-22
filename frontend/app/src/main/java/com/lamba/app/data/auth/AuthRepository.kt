package com.lamba.app.data.auth

class AuthRepository(
    private val api: AuthApi = AuthNetwork.api
) {
    suspend fun register(
        name: String,
        email: String,
        password: String
    ): UserResponse {
        return api.register(
            RegisterRequest(
                email = email,
                password = password,
                fullName = name.ifBlank { null }
            )
        )
    }

    suspend fun login(email: String, password: String): TokenResponse {
        return api.login(LoginRequest(email = email, password = password))
    }

    suspend fun me(accessToken: String): UserResponse {
        return api.me("Bearer $accessToken")
    }
}
