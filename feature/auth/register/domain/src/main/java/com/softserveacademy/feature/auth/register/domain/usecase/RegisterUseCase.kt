package com.softserveacademy.feature.auth.register.domain.usecase

import com.softserveacademy.core.domain.model.User
import com.softserveacademy.core.domain.util.Validator
import com.softserveacademy.feature.auth.register.domain.repository.RegisterRepository

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
        
        if (!Validator.isAtLeast18(user.birthDate)) {
            return Result.failure(IllegalArgumentException("You must be at least 18 years old to register"))
        }

        if (user.email.isBlank()) {
            return Result.failure(IllegalArgumentException("Email cannot be empty"))
        }
        
        if (!Validator.isValidEmail(user.email)) {
            return Result.failure(IllegalArgumentException("Invalid email format"))
        }
        if (password.length < 6) {
            return Result.failure(IllegalArgumentException("Password must be at least 6 characters long"))
        }
        return repository.register(user, password)
    }
}
