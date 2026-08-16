package com.softserveacademy.profile.data.repository

import com.softserveacademy.profile.domain.model.UserProfile
import com.softserveacademy.profile.domain.repository.ProfileRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.Serializable
import javax.inject.Inject

/**
 * Implementation of [ProfileRepository] providing profile data from Supabase.
 */
class ProfileRepositoryImpl @Inject constructor(
    private val supabase: SupabaseClient
) : ProfileRepository {

    override suspend fun getProfile(): Result<UserProfile> {
        return try {
            val user = supabase.auth.currentUserOrNull()
                ?: return Result.failure(Exception("User not authenticated"))

            val profileDto = supabase.postgrest["profiles"]
                .select {
                    filter {
                        eq("id", user.id)
                    }
                }
                .decodeSingle<ProfileDto>()

            Result.success(profileDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(profile: UserProfile, password: String?): Result<Unit> {
        return try {
            val user = supabase.auth.currentUserOrNull()
                ?: return Result.failure(Exception("User not authenticated"))

            val profileDto = ProfileDto.fromDomain(user.id, profile)
            
            supabase.postgrest["profiles"].update(profileDto) {
                filter {
                    eq("id", user.id)
                }
            }

            if (!password.isNullOrBlank()) {
                supabase.auth.updateUser {
                    this.password = password
                }
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
        val email: String? = null,
        val phone: String? = null,
        val birth_date: Long? = null,
        val points: Int = 0,
        val avatar_url: String? = null,
        val location: String? = null,
        val is_birth_date_changed: Boolean = false
    ) {
        fun toDomain() = UserProfile(
            firstName = first_name,
            lastName = last_name,
            points = points,
            avatarUrl = avatar_url ?: "",
            phone = phone,
            birthDate = birth_date,
            isBirthDateChanged = is_birth_date_changed,
            location = location
        )

        companion object {
            fun fromDomain(id: String, domain: UserProfile) = ProfileDto(
                id = id,
                first_name = domain.firstName,
                last_name = domain.lastName,
                phone = domain.phone,
                birth_date = domain.birthDate,
                points = domain.points,
                avatar_url = domain.avatarUrl,
                location = domain.location,
                is_birth_date_changed = domain.isBirthDateChanged
            )
        }
    }
}
