package io.primer.ipay88.internal.extension

import com.ipay.IPayIHPayment
import com.ipay.IPayIHR
import io.primer.ipay88.api.ui.IPay88LauncherParams

private const val PAYMENT_ID_CREDIT_CARD = "2"

internal fun IPay88LauncherParams.toIPayIHPayment() = IPayIHPayment().let {
    it.amount = amount
    it.refNo = referenceNumber
    it.paymentId = PAYMENT_ID_CREDIT_CARD
    it.fixpaymentid = PAYMENT_ID_CREDIT_CARD
    it.userName = customerId
    it.userEmail = customerEmail
    it.currency = currencyCode
    it.country = countryCode
    it.merchantCode = merchantCode
    it.prodDesc = prodDesc
    it.backendPostURL = backendCallbackUrl
    it
}

internal fun IPay88LauncherParams.toIPayIHR() = IPayIHR().let {
    it.amount = amount
    it.refNo = referenceNumber
    it.merchantCode = merchantCode
    it.country_Code = countryCode
    it
}