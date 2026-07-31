package com.softserveacademy.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.AppTheme
import com.softserveacademy.core.domain.model.HotelDetails
import com.softserveacademy.core.domain.model.IncludedItem
import com.softserveacademy.core.domain.model.TravelItemType
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.domain.repository.TourRepo
import com.softserveacademy.core.domain.usecase.GetThemeUseCase
import com.softserveacademy.core.error.extension.map
import com.softserveacademy.core.error.extension.onFailure
import com.softserveacademy.core.error.extension.onSuccess
import com.softserveacademy.home.presentation.state.HotelDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HotelDetailsViewModel @Inject constructor(
    private val hotelRepo: HotelRepo,
    private val tourRepo: TourRepo,
    getThemeUseCase: GetThemeUseCase
) : ViewModel() {
    private val _hotelDetailState = MutableStateFlow(HotelDetailState.IsLoading() as HotelDetailState)
    val hotelDetailState = _hotelDetailState.asStateFlow()

    val appTheme = getThemeUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppTheme.SYSTEM)

    fun getHotelDetail(id: String, type: TravelItemType = TravelItemType.HOTEL) {
        viewModelScope.launch {
            _hotelDetailState.update{
                HotelDetailState.IsLoading(true)
            }
            delay(1000)
            
            val result = when (type) {
                TravelItemType.HOTEL -> hotelRepo.getHotelById(id.toInt())
                TravelItemType.TOUR -> tourRepo.getTourById(id).map { tour ->
                    HotelDetails(
                        id = tour.id.hashCode(),
                        minimumPrice = tour.price.toInt(),
                        imageList = listOf(tour.imageUrl),
                        name = tour.title,
                        description = tour.description,
                        rating = tour.rating.toDouble(),
                        address = tour.location,
                        numberOfReviews = (tour.rating * 10).toInt(),
                        includedItems = emptyList(),
                        latitude = 0.0,
                        longitude = 0.0
                    )
                }
            }

            result.onSuccess { hotelDetails ->
                    _hotelDetailState.update {
                        HotelDetailState.Data(hotelDetails)
                    }
                }
                .onFailure { error ->
                    _hotelDetailState.update {
                        HotelDetailState.Error(error.toString())
                    }
                }
        }
    }
}
