package com.softserveacademy.home.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.Poi
import com.softserveacademy.core.domain.repository.PoiRepo
import com.softserveacademy.core.error.model.AppResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PoiDetailsViewModel"

data class PoiDetailsState(
    val poi: Poi? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class PoiDetailsViewModel @Inject constructor(
    private val poiRepo: PoiRepo
) : ViewModel() {

    private val _state = MutableStateFlow(PoiDetailsState())
    val state: StateFlow<PoiDetailsState> = _state.asStateFlow()

    fun loadPoi(name: String, lat: Double, lon: Double) {
        Log.d(TAG, "loadPoi: Loading details for $name ($lat, $lon)")
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            
            // For now, since we don't have a "getPoiById", we search nearby with a very small radius
            // to find the exact match or we rely on the data passed. 
            // In a real scenario, PoiRepo would have getPoiById or similar.
            
            // As a placeholder, we'll try to get more details if needed, 
            // but for now let's just use the basic info or find it in nearby.
            when (val result = poiRepo.getNearbyPlaces(lat, lon, 100.0)) {
                is AppResult.Success -> {
                    val match = result.data.find { it.name == name }
                    if (match != null) {
                        Log.d(TAG, "loadPoi: Match found with full details")
                        _state.update { it.copy(poi = match, isLoading = false) }
                    } else {
                        Log.w(TAG, "loadPoi: No exact match found in nearby search")
                        _state.update { it.copy(isLoading = false) }
                    }
                }
                is AppResult.Failure -> {
                    Log.e(TAG, "loadPoi: Failed to fetch details: ${result.error}")
                    _state.update { it.copy(isLoading = false, errorMessage = "Failed to load details") }
                }
            }
        }
    }
}
