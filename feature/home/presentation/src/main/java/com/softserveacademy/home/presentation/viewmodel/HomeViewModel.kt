package com.softserveacademy.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.home.domain.usecases.GetRecommendedHotelsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the state and business logic of the Home screen.
 *
 * It coordinates the loading of the primary hotel list displayed on the main dashboard.
 *
 * @param getRecommendedHotelsUseCase The use case used to fetch the collection of recommended hotels.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecommendedHotelsUseCase: GetRecommendedHotelsUseCase
) : ViewModel() {

    private val _hotels = MutableStateFlow<List<Hotel>>(emptyList())

    /**
     * Observable state containing the list of [Hotel] objects to be displayed.
     */
    val hotels = _hotels.asStateFlow()

    init {
        loadHotels()
    }

    /**
     * Initiates the loading of all available hotels from the use case.
     */
    private fun loadHotels() {
        viewModelScope.launch {
            getRecommendedHotelsUseCase().onSuccess { recommendedHotels ->
                _hotels.value = recommendedHotels
            }
        }
    }
}
