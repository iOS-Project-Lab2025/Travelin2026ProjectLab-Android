package com.softserveacademy.core.error.model

sealed interface UiText {
    data class Raw(val value: String) : UiText
    data class Resource(val resId: Int, val args: List<Any> = emptyList()) : UiText
}
