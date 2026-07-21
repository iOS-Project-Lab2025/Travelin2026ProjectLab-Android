package com.softserveacademy.feature.booking.common.domain

import com.softserveacademy.feature.booking.common.domain.usecase.ValidateContactInfoUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidateContactInfoUseCaseTest {

    private val useCase = ValidateContactInfoUseCase()

    @Test
    fun `validateFirstName returns Success when first name is not blank`() {
        val result = useCase.validateFirstName("John")
        assertTrue(result is ValidateContactInfoUseCase.ValidationResult.Success)
    }

    @Test
    fun `validateFirstName returns Invalid when first name is blank`() {
        val result = useCase.validateFirstName(" ")
        assertTrue(result is ValidateContactInfoUseCase.ValidationResult.Invalid)
        assertEquals("First name is required", (result as ValidateContactInfoUseCase.ValidationResult.Invalid).errorMessage)
    }

    @Test
    fun `validateLastName returns Success when last name is not blank`() {
        val result = useCase.validateLastName("Doe")
        assertTrue(result is ValidateContactInfoUseCase.ValidationResult.Success)
    }

    @Test
    fun `validateLastName returns Invalid when last name is blank`() {
        val result = useCase.validateLastName("")
        assertTrue(result is ValidateContactInfoUseCase.ValidationResult.Invalid)
        assertEquals("Last name is required", (result as ValidateContactInfoUseCase.ValidationResult.Invalid).errorMessage)
    }

    @Test
    fun `validateEmail returns Success when email is valid`() {
        val result = useCase.validateEmail("john.doe@example.com")
        assertTrue(result is ValidateContactInfoUseCase.ValidationResult.Success)
    }

    @Test
    fun `validateEmail returns Invalid when email is blank`() {
        val result = useCase.validateEmail("")
        assertTrue(result is ValidateContactInfoUseCase.ValidationResult.Invalid)
        assertEquals("Email is required", (result as ValidateContactInfoUseCase.ValidationResult.Invalid).errorMessage)
    }

    @Test
    fun `validateEmail returns Invalid when email format is invalid`() {
        val result = useCase.validateEmail("invalid-email")
        assertTrue(result is ValidateContactInfoUseCase.ValidationResult.Invalid)
        assertEquals("Invalid email format", (result as ValidateContactInfoUseCase.ValidationResult.Invalid).errorMessage)
    }

    @Test
    fun `validatePhoneNumber returns Success when phone number is not blank`() {
        val result = useCase.validatePhoneNumber("123456789")
        assertTrue(result is ValidateContactInfoUseCase.ValidationResult.Success)
    }

    @Test
    fun `validatePhoneNumber returns Invalid when phone number is blank`() {
        val result = useCase.validatePhoneNumber("")
        assertTrue(result is ValidateContactInfoUseCase.ValidationResult.Invalid)
        assertEquals("Phone number is required", (result as ValidateContactInfoUseCase.ValidationResult.Invalid).errorMessage)
    }
}