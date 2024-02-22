package com.droidcon.forecaster

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TestDoubles {

    interface EmailValidator {
        fun validate(email: String): Boolean
    }

    class AuthorizationSystem(
        private val emailValidator: EmailValidator
    ) {
        fun authorizedUsersCount(): Int {
            return 0
        }

        fun authorize(email: String, password: String): Boolean {
            if (emailValidator.validate(email)) {
                return email.isNotBlank() && password.isNotBlank()
            }
            return false
        }
    }

    @Test fun noAuthorizedUsersInitially() {
        val system = AuthorizationSystem(DummyEmailValidator())

        val authorizedUsersCount = system.authorizedUsersCount()

        assertThat(authorizedUsersCount).isEqualTo(0)
    }

    @Test fun successfulAuthorization() {
        val email = "an email"
        val password = "a password"
        val emailValidator = PresetEmailValidator(listOf(email))
        val system = AuthorizationSystem(emailValidator)

        val authorizationResult = system.authorize(email, password)

        assertThat(authorizationResult).isTrue()
    }

    class PresetEmailValidator(
        private val validEmails: List<String>
    ) : EmailValidator {
        override fun validate(email: String): Boolean {
            return validEmails.contains(email)
        }
    }

    class AcceptingEmailValidationMock: EmailValidator {
        private var validationCallsCount = 0

        override fun validate(email: String): Boolean {
            validationCallsCount++
            return true
        }

        fun verify(timesCalled: Int) {
            if (timesCalled != validationCallsCount) {
                throw AssertionError(
                    "Incorrect count of calls to validate. " +
                            "Expected $timesCalled, but was $validationCallsCount"
                )
            }
        }
    }

    class AcceptingEmailValidationSpy: EmailValidator {
        var validationWasCalled = false

        override fun validate(email: String): Boolean {
            validationWasCalled = true
            return true
        }
    }

    class AcceptingEmailValidator : EmailValidator {
        override fun validate(email: String): Boolean {
            return true
        }
    }

    class DummyEmailValidator : EmailValidator {
        override fun validate(email: String): Boolean {
            TODO("Not yet implemented")
        }
    }
}