package io.primer.ipay88.api.ui

import java.io.Serializable

data class IPay88LauncherParams(
    val merchantCode: String,
    val amount: String,
    val referenceNumber: String,
    val prodDesc: String,
    val currencyCode: String?,
    val countryCode: String?,
    val customerId: String?,
    val customerEmail: String?,
    val backendCallbackUrl: String,
    val errorCode: Int
) : Serializable
