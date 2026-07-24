
package com.softserveacademy.home.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.home.domain.repository.SearchFilter
import com.softserveacademy.home.domain.repository.SearchItem
import com.softserveacademy.home.domain.repository.SearchRepository
import com.softserveacademy.home.presentation.util.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    var searchQuery by mutableStateOf("")
    var currentFilter by mutableStateOf(SearchFilter.ALL)
    var uiState by mutableStateOf<SearchUiState>(SearchUiState.Idle)

    private var searchJob: Job? = null

    init {
        performSearch(isInitial = true)
    }

    fun onQueryChanged(newQuery: String) {
        searchQuery = newQuery
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500.milliseconds)
            performSearch()
        }
    }

    fun onFilterChanged(newFilter: SearchFilter) {
        currentFilter = newFilter
        performSearch()
    }

    fun performSearch(isInitial: Boolean = false) {
        if (!networkMonitor.isConnected()) {
            uiState = SearchUiState.Error("No internet connection.")
            return
        }
        viewModelScope.launch {
            uiState = SearchUiState.Loading
            val location = if (isInitial) "Santiago" else null

            searchRepository.search(searchQuery, currentFilter, location).onSuccess { results ->
                uiState = if (results.isEmpty()) SearchUiState.Empty else SearchUiState.Success(results)
            }.onFailure {
                uiState = SearchUiState.Error("An error occurred. Please try again.")
            }
        }
    }
}

sealed class SearchUiState {
    data object Idle : SearchUiState()
    data object Loading : SearchUiState()
    data object Empty : SearchUiState()
    data class Success(val items: List<SearchItem>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}