package com.softserveacademy.feature.auth.login.data.repository

import com.softserveacademy.feature.auth.login.domain.repository.LoginRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class LoginRepositoryImpl(
    private val supabase: SupabaseClient
) : LoginRepository {

    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Result.success(Unit)
        } catch (e: Exception) {
            val message = e.message ?: ""
            if (message.contains("invalid_credentials", ignoreCase = true) ||
                message.contains("Invalid login credentials", ignoreCase = true)) {
                Result.failure(Exception("Incorrect email or password. Please try again."))
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun recoverPassword(email: String): Result<Unit> {
        return try {
            supabase.auth.resetPasswordForEmail(email)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
