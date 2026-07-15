package com.softserveacademy.home.data.repository

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [ProfileRepositoryImpl].
 */
class ProfileRepositoryImplTest {

    private val repository = ProfileRepositoryImpl()

    @Test
    fun `given getProfile called when invoked then returns success with mock profile`() = runTest {
        // WHEN: getProfile is called
        val result = repository.getProfile()

        // THEN: It should return success with the expected mock data
        assertTrue(result.isSuccess)
        val profile = result.getOrNull()
        assertEquals("John Doe", profile?.name)
        assertEquals("Mars, Solar System", profile?.location)
    }
}
