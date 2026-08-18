package com.softserveacademy.profile.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [ProfileRepositoryImpl].
 *
 * Verifies the business logic of the profile repository, specifically
 * the interaction with the Supabase client.
 */
class ProfileRepositoryImplTest {

    private val supabase: SupabaseClient = mockk()
    private lateinit var repository: ProfileRepositoryImpl

    @Before
    fun setup() {
        repository = ProfileRepositoryImpl(supabase)
        // We mock the Supabase Auth extension property
        mockkStatic("io.github.jan.supabase.auth.AuthKt")
    }

    @Test
    fun `given user not authenticated when getProfile called then returns failure`() = runTest {
        // GIVEN: Supabase reports no current user session
        val mockAuth = mockk<Auth>()
        every { supabase.auth } returns mockAuth
        every { mockAuth.currentUserOrNull() } returns null

        // WHEN: getProfile is invoked
        val result = repository.getProfile()

        // THEN: It should return a failure result with the appropriate message
        assertTrue(result.isFailure)
        assertEquals("User not authenticated", result.exceptionOrNull()?.message)
    }

    @Test
    fun `given user not authenticated when updateProfile called then returns failure`() = runTest {
        // GIVEN: Supabase reports no current user session
        val mockAuth = mockk<Auth>()
        every { supabase.auth } returns mockAuth
        every { mockAuth.currentUserOrNull() } returns null

        // WHEN: updateProfile is invoked
        val result = repository.updateProfile(mockk(), null)

        // THEN: It should return a failure result with the appropriate message
        assertTrue(result.isFailure)
        assertEquals("User not authenticated", result.exceptionOrNull()?.message)
    }
}
