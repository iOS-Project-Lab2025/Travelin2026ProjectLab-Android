package com.softserveacademy.core.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.error.extension.onFailure
import com.softserveacademy.core.error.extension.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HorizontalCardViewModel @Inject constructor(
    private val hotelRepo: HotelRepo
) : ViewModel() {
    private val _horizontalCardState = MutableStateFlow(HorizontalCardState.IsLoading() as HorizontalCardState)
    val horizontalCardState = _horizontalCardState.asStateFlow()

    fun getHotel(id: Int?) {
        viewModelScope.launch {
            _horizontalCardState.update{
                HorizontalCardState.IsLoading(true)
            }
            hotelRepo.getHotelById(id ?: 1)
                .onSuccess { hotelDetails ->
                    _horizontalCardState.update {
                        HorizontalCardState.Data(hotelDetails.toSummary())
                    }
                }
                .onFailure { error ->
                    _horizontalCardState.update {
                        HorizontalCardState.Error(error.toString())
                    }
                }
        }
    }
}
