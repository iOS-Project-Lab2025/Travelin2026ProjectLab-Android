package com.softserveacademy.feature.auth.register.data.repository

import com.softserveacademy.core.domain.model.User
import com.softserveacademy.feature.auth.register.domain.repository.RegisterRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.Serializable

class RegisterRepositoryImpl(
    private val supabase: SupabaseClient
) : RegisterRepository {

    override suspend fun register(user: User, password: String): Result<Unit> {
        return try {
            // 1. Sign up user in Supabase Auth
            // For Email provider, authUser contains user info if confirmation is required,
            // or it might be null if auto-confirm is enabled (as it becomes a sign-in result).
            val authUser = supabase.auth.signUpWith(Email) {
                this.email = user.email
                this.password = password
            }

            // 2. Determine UID (handling both confirm-required and auto-confirm cases)
            val uid = authUser?.id ?: supabase.auth.currentUserOrNull()?.id
            
            if (uid != null) {
                val profile = ProfileDto(
                    id = uid,
                    first_name = user.firstName,
                    last_name = user.lastName,
                    email = user.email,
                    phone = user.phone,
                    birth_date = user.birthDate
                )
                // 3. Attempt to insert the profile row.
                // Note: This will fail if RLS is enabled and there is no authenticated session yet
                // (which happens if Email Confirmation is required and not yet clicked).
                supabase.postgrest["profiles"].insert(profile)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Serializable
    private data class ProfileDto(
        val id: String,
        val first_name: String,
        val last_name: String,
        val email: String,
        val phone: String,
        val birth_date: Long
    )
}
