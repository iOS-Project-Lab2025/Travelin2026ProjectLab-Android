package com.softserveacademy.home.presentation.viewmodel

import android.util.Log
import com.softserveacademy.home.domain.repository.SearchFilter
import com.softserveacademy.home.domain.repository.SearchItem
import com.softserveacademy.home.domain.repository.SearchRepository
import com.softserveacademy.home.presentation.util.NetworkMonitor
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val searchRepository = mockk<SearchRepository>()
    private val networkMonitor = mockk<NetworkMonitor>()
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Mock static Android Log to avoid "Method d in android.util.Log not mocked"
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Log::class)
    }

    @Test
    fun `given connected when init then performs initial search with Santiago location`() = runTest {
        every { networkMonitor.isConnected() } returns true
        coEvery {
            searchRepository.search(any(), any(), any(), any(), any(), any())
        } returns Result.success(emptyList())

        viewModel = SearchViewModel(searchRepository, networkMonitor)

        // Initial call in init{} passes isInitial=true, which sets location="Santiago"
        coVerify {
            searchRepository.search("", SearchFilter.ALL, "Santiago", null, null, null)
        }
    }

    @Test
    fun `given no connection when init then state is Error`() = runTest {
        every { networkMonitor.isConnected() } returns false

        viewModel = SearchViewModel(searchRepository, networkMonitor)

        assertTrue(viewModel.uiState is SearchUiState.Error)
        assertEquals("No internet connection.", (viewModel.uiState as SearchUiState.Error).message)
    }

    @Test
    fun `given results when search then state is Success`() = runTest {
        val items = listOf(mockk<SearchItem>())
        every { networkMonitor.isConnected() } returns true
        coEvery {
            searchRepository.search(any(), any(), any(), any(), any(), any())
        } returns Result.success(items)

        viewModel = SearchViewModel(searchRepository, networkMonitor)

        assertTrue(viewModel.uiState is SearchUiState.Success)
        assertEquals(items, (viewModel.uiState as SearchUiState.Success).items)
    }

    @Test
    fun `given connected when filter changed then searches with new filter and null location`() = runTest {
        every { networkMonitor.isConnected() } returns true
        coEvery {
            searchRepository.search(any(), any(), any(), any(), any(), any())
        } returns Result.success(emptyList())

        viewModel = SearchViewModel(searchRepository, networkMonitor)

        // Change filter
        viewModel.onFilterChanged(SearchFilter.HOTELS)

        // After initial search, performSearch() is called with isInitial=false (default)
        // so location should be null
        coVerify {
            searchRepository.search("", SearchFilter.HOTELS, null, null, null, null)
        }
    }

    @Test
    fun `given nearby mode enabled when search then passes coordinates and radius`() = runTest {
        val lat = -33.4489
        val lon = -70.6693
        every { networkMonitor.isConnected() } returns true
        coEvery {
            searchRepository.search(any(), any(), any(), any(), any(), any())
        } returns Result.success(emptyList())

        viewModel = SearchViewModel(searchRepository, networkMonitor)

        // Toggle nearby mode
        viewModel.toggleNearbyMode(enabled = true, lat = lat, lon = lon)

        coVerify {
            searchRepository.search(
                query = "",
                filter = SearchFilter.ALL,
                location = null,
                latitude = lat,
                longitude = lon,
                radius = 10.0 // default radius is 10f
            )
        }
        assertTrue(viewModel.isNearbyMode)
    }

    @Test
    fun `given POIS filter when search then passes coordinates even if nearby mode is off`() = runTest {
        val lat = -33.4489
        val lon = -70.6693
        every { networkMonitor.isConnected() } returns true
        coEvery {
            searchRepository.search(any(), any(), any(), any(), any(), any())
        } returns Result.success(emptyList())

        viewModel = SearchViewModel(searchRepository, networkMonitor)

        // Manually set coordinates (simulating a previous location fix)
        viewModel.currentLatitude = lat
        viewModel.currentLongitude = lon

        // Change filter to POIS
        viewModel.onFilterChanged(SearchFilter.POIS)

        coVerify {
            searchRepository.search(
                query = "",
                filter = SearchFilter.POIS,
                location = null,
                latitude = lat,
                longitude = lon,
                radius = 10.0
            )
        }
    }

    @Test
    fun `when permission denied then state is PermissionDenied`() = runTest {
        every { networkMonitor.isConnected() } returns true
        coEvery {
            searchRepository.search(any(), any(), any(), any(), any(), any())
        } returns Result.success(emptyList())

        viewModel = SearchViewModel(searchRepository, networkMonitor)
        viewModel.onPermissionDenied()

        assertTrue(viewModel.uiState is SearchUiState.PermissionDenied)
    }
}