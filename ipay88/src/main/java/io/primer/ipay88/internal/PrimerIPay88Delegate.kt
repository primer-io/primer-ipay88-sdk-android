package io.primer.ipay88.internal

import com.ipay.IPayIHResultDelegate
import java.io.Serializable

internal class PrimerIPay88Delegate :
    IPayIHResultDelegate, Serializable {

    override fun onPaymentSucceeded(
        transId: String?,
        refNo: String?,
        amount: String?,
        remark: String?,
        authCode: String?,
    ) {
        IPayStateHolder.currentState = IPayPaymentState.Success
    }

    override fun onPaymentFailed(
        transId: String?,
        refNo: String?,
        amount: String?,
        remark: String?,
        errorDescription: String?,
    ) {
        IPayStateHolder.currentState = IPayPaymentState.Failed(transId, refNo, errorDescription)
    }

    override fun onPaymentCanceled(
        transId: String?,
        refNo: String?,
        amount: String?,
        remark: String?,
        errorDescription: String?,
    ) {
        IPayStateHolder.currentState = IPayPaymentState.Cancelled(transId, errorDescription)
    }

    override fun onRequeryResult(
        merchantCode: String?,
        refNo: String?,
        amount: String?,
        result: String?
    ) {
        IPayStateHolder.currentState = IPayPaymentState.ReQuery
    }

    override fun onConnectionError(
        merchantCode: String?,
        refNo: String?,
        amount: String?,
        remark: String?,
        lang: String?,
        country: String?
    ) {
        IPayStateHolder.currentState = IPayPaymentState.ConnectionError
    }
}

internal sealed class IPayPaymentState {
    object Success : IPayPaymentState()
    data class Failed(val transId: String?, val refNo: String?, val errorDescription: String?) :
        IPayPaymentState()

    data class Cancelled(val transId: String?, val errorDescription: String?) : IPayPaymentState()
    object ReQuery : IPayPaymentState()
    object ConnectionError : IPayPaymentState()
}