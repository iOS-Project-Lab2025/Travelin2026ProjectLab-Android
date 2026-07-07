package com.softserveacademy.home.presentation.state

sealed interface SectionState<out T> {
    data object Loading : SectionState<Nothing>
    data class Success<T>(val data: T) : SectionState<T>
    data class Error(val message: String) : SectionState<Nothing>
    data object Empty : SectionState<Nothing>
}