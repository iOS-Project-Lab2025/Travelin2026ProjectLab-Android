package com.softserveacademy.feature.auth.common.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.softserveacademy.feature.auth.common.domain.AuthToken
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class SessionRepositoryImplTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var sessionRepository: SessionRepositoryImpl

    @Before
    fun setup() {
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { File(temporaryFolder.newFolder(), "test.preferences_pb") }
        )
        sessionRepository = SessionRepositoryImpl(dataStore)
    }

    @Test
    fun `isLoggedIn returns false initially`() = runTest(testDispatcher) {
        val isLoggedIn = sessionRepository.isLoggedIn().first()
        assertFalse(isLoggedIn)
    }

    @Test
    fun `isLoggedIn returns true after saving tokens`() = runTest(testDispatcher) {
        val token = AuthToken("access", "refresh")
        sessionRepository.saveTokens(token)
        
        val isLoggedIn = sessionRepository.isLoggedIn().first()
        assertTrue(isLoggedIn)
    }

    @Test
    fun `getAccessToken returns saved token`() = runTest(testDispatcher) {
        val token = AuthToken("access", "refresh")
        sessionRepository.saveTokens(token)
        
        val accessToken = sessionRepository.getAccessToken().first()
        assertEquals("access", accessToken)
    }

    @Test
    fun `clearTokens removes tokens and updates isLoggedIn`() = runTest(testDispatcher) {
        val token = AuthToken("access", "refresh")
        sessionRepository.saveTokens(token)
        
        sessionRepository.clearTokens()
        
        val accessToken = sessionRepository.getAccessToken().first()
        val isLoggedIn = sessionRepository.isLoggedIn().first()
        
        assertNull(accessToken)
        assertFalse(isLoggedIn)
    }

    @Test
    fun `logout clears tokens`() = runTest(testDispatcher) {
        val token = AuthToken("access", "refresh")
        sessionRepository.saveTokens(token)
        
        sessionRepository.logout()
        
        val accessToken = sessionRepository.getAccessToken().first()
        assertNull(accessToken)
    }
}
