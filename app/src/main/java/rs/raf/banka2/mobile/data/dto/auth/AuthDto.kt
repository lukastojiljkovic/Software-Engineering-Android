package rs.raf.banka2.mobile.data.dto.auth

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    val email: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String? = null,
    val expiresIn: Long? = null
)

@JsonClass(generateAdapter = true)
data class RefreshRequest(
    val refreshToken: String
)

@JsonClass(generateAdapter = true)
data class PasswordResetRequest(
    val email: String
)

@JsonClass(generateAdapter = true)
data class PasswordResetConfirmRequest(
    val token: String,
    val newPassword: String
)

@JsonClass(generateAdapter = true)
data class ActivateAccountRequest(
    val token: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class MessageResponse(
    val message: String? = null
)

/**
 * Spec Sc 9 + ad-hoc bug 12.05.2026: pre nego sto Mobile renderuje aktivaciju
 * forme, FE poziva GET /auth-employee/activation-token/{token}/status da
 * proveri da li je token jos validan. BE vraca jedan od:
 *   - VALID         — token postoji, nije iskoriscen, nije istekao
 *   - USED          — token je vec iskoriscen (uspesna aktivacija u proslosti)
 *   - EXPIRED       — token postoji ali je istekao (>24h od kreacije)
 *   - INVALID       — token ne postoji u DB (pogresan link)
 *   - ALREADY_ACTIVE — token je validan ali je nalog vec aktivan iz druge sesije
 */
@JsonClass(generateAdapter = true)
data class ActivationTokenStatusResponse(
    val status: String,
    val expiresAt: String? = null,
    val email: String? = null
)
