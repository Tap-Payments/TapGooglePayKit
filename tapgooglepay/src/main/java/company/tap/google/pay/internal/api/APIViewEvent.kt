package company.tap.google.pay.internal.api

import androidx.annotation.RestrictTo

@RestrictTo(RestrictTo.Scope.LIBRARY)
sealed class APIViewEvent {
    object CreateTokenEvent :APIViewEvent()
}