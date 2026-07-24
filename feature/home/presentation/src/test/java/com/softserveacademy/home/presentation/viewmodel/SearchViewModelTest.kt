package com.softserveacademy.home.presentation.viewmodel

import com.softserveacademy.home.domain.repository.SearchFilter
import com.softserveacademy.home.domain.repository.SearchItem
import com.softserveacademy.home.domain.repository.SearchRepository
import com.softserveacademy.home.presentation.util.NetworkMonitor
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
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
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given connected when init then performs initial search with Santiago location`() = runTest {
        every { networkMonitor.isConnected() } returns true
        coEvery { searchRepository.search(any(), any(), any()) } returns Result.success(emptyList())

        viewModel = SearchViewModel(searchRepository, networkMonitor)

        coVerify { searchRepository.search("", SearchFilter.ALL, "Santiago") }
    }

    @Test
    fun `given no connection when init then state is Error`() = runTest {
        every { networkMonitor.isConnected() } returns false

        viewModel = SearchViewModel(searchRepository, networkMonitor)

        assertTrue(viewModel.uiState is SearchUiState.Error)
        assertEquals("No internet connection.", (viewModel.uiState as SearchUiState.Error).message)
    }

    @Test
    fun `given empty results when search then state is Empty`() = runTest {
        every { networkMonitor.isConnected() } returns true
        coEvery { searchRepository.search(any(), any(), any()) } returns Result.success(emptyList())

        viewModel = SearchViewModel(searchRepository, networkMonitor)

        assertTrue(viewModel.uiState is SearchUiState.Empty)
    }

    @Test
    fun `given results when search then state is Success`() = runTest {
        val items = listOf(mockk<SearchItem>())
        every { networkMonitor.isConnected() } returns true
        coEvery { searchRepository.search(any(), any(), any()) } returns Result.success(items)

        viewModel = SearchViewModel(searchRepository, networkMonitor)

        assertTrue(viewModel.uiState is SearchUiState.Success)
        assertEquals(items, (viewModel.uiState as SearchUiState.Success).items)
    }

    @Test
    fun `given search error when search then state is Error`() = runTest {
        every { networkMonitor.isConnected() } returns true
        coEvery { searchRepository.search(any(), any(), any()) } returns Result.failure(Exception())

        viewModel = SearchViewModel(searchRepository, networkMonitor)

        assertTrue(viewModel.uiState is SearchUiState.Error)
        assertEquals("An error occurred. Please try again.", (viewModel.uiState as SearchUiState.Error).message)
    }

    @Test
    fun `given connected when filter changed then searches with new filter`() = runTest {
        every { networkMonitor.isConnected() } returns true
        coEvery { searchRepository.search(any(), any(), any()) } returns Result.success(emptyList())

        viewModel = SearchViewModel(searchRepository, networkMonitor)
        viewModel.onFilterChanged(SearchFilter.HOTELS)

        coVerify { searchRepository.search("", SearchFilter.HOTELS, null) }
    }
}
