package io.primer.ipay88.api.exceptions

class IPayPaymentFailedException(
    val transactionId: String?,
    val refNo: String?,
    val errorDescription: String?
) : IllegalStateException()
