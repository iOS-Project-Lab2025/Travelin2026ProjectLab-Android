
package com.softserveacademy.home.presentation.viewmodel

import android.util.Log
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

private const val TAG = "SearchViewModel"

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    var searchQuery by mutableStateOf("")
    var currentFilter by mutableStateOf(SearchFilter.ALL)
    var uiState by mutableStateOf<SearchUiState>(SearchUiState.Idle)

    var isNearbyMode by mutableStateOf(false)
    var searchRadius by mutableStateOf(10f) // Radius in km
    var currentLatitude by mutableStateOf<Double?>(null)
    var currentLongitude by mutableStateOf<Double?>(null)

    private var searchJob: Job? = null
    private var radiusJob: Job? = null

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

    fun onRadiusChanged(newRadius: Float) {
        searchRadius = newRadius
        radiusJob?.cancel()
        radiusJob = viewModelScope.launch {
            delay(800.milliseconds)
            performSearch()
        }
    }

    fun onPermissionDenied() {
        uiState = SearchUiState.PermissionDenied
    }

    fun toggleNearbyMode(enabled: Boolean, lat: Double? = null, lon: Double? = null) {
        isNearbyMode = enabled
        currentLatitude = lat
        currentLongitude = lon
        performSearch()
    }

    fun performSearch(isInitial: Boolean = false) {
        if (!networkMonitor.isConnected()) {
            uiState = SearchUiState.Error("No internet connection.")
            return
        }
        viewModelScope.launch {
            uiState = SearchUiState.Loading 
            
            val location = if (isInitial && !isNearbyMode) "Santiago" else null
            val lat = if (isNearbyMode || currentFilter == SearchFilter.POIS) currentLatitude else null
            val lon = if (isNearbyMode || currentFilter == SearchFilter.POIS) currentLongitude else null
            val rad = if (isNearbyMode || currentFilter == SearchFilter.POIS) searchRadius.toDouble() else null

            Log.d(TAG, "performSearch: query=$searchQuery, filter=$currentFilter, lat=$lat, lon=$lon, radius=$rad")

            searchRepository.search(
                query = searchQuery,
                filter = currentFilter,
                location = location,
                latitude = lat,
                longitude = lon,
                radius = rad
            ).onSuccess { results ->
                Log.d(TAG, "performSearch Success: found ${results.size} items total")
                uiState = if (results.isEmpty()) SearchUiState.Empty else SearchUiState.Success(results)
            }.onFailure { e ->
                Log.e(TAG, "performSearch Failure: ${e.message}", e)
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
    data object PermissionDenied : SearchUiState()
}
