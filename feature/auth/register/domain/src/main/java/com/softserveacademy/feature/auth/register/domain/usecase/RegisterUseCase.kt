package com.softserveacademy.feature.auth.register.domain.usecase

import com.softserveacademy.core.domain.model.User
import com.softserveacademy.feature.auth.register.domain.repository.RegisterRepository
import java.util.Calendar

class RegisterUseCase(
    private val repository: RegisterRepository
) {
    suspend operator fun invoke(user: User, password: String): Result<Unit> {
        if (user.firstName.isBlank()) {
            return Result.failure(IllegalArgumentException("First name cannot be empty"))
        }
        if (user.lastName.isBlank()) {
            return Result.failure(IllegalArgumentException("Last name cannot be empty"))
        }
        if (user.phone.isBlank()) {
            return Result.failure(IllegalArgumentException("Phone number cannot be empty"))
        }
        
        if (!isAtLeast18(user.birthDate)) {
            return Result.failure(IllegalArgumentException("You must be at least 18 years old to register"))
        }

        if (user.email.isBlank()) {
            return Result.failure(IllegalArgumentException("Email cannot be empty"))
        }
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
        if (!emailRegex.matches(user.email)) {
            return Result.failure(IllegalArgumentException("Invalid email format"))
        }
        if (password.length < 6) {
            return Result.failure(IllegalArgumentException("Password must be at least 6 characters long"))
        }
        return repository.register(user, password)
    }

    private fun isAtLeast18(birthDate: Long): Boolean {
        val today = Calendar.getInstance()
        val birthCalendar = Calendar.getInstance().apply { timeInMillis = birthDate }
        
        var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)
        
        if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        
        return age >= 18
    }
}
