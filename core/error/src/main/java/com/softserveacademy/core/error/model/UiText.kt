package com.softserveacademy.core.error.model

/** Represents text that can be displayed in the UI, either raw or as a string resource for localisation. */
sealed interface UiText {
    data class Raw(val value: String) : UiText
    data class Resource(val resId: Int, val args: List<Any> = emptyList()) : UiText
}
