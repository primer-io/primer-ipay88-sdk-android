package io.primer.ipay88.api.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ipay.IPayIH
import io.primer.ipay88.api.exceptions.IPayConnectionErrorException
import io.primer.ipay88.api.exceptions.IPayPaymentCancelledException
import io.primer.ipay88.api.exceptions.IPayPaymentFailedException
import io.primer.ipay88.internal.IPayPaymentState
import io.primer.ipay88.internal.IPayStateHolder
import io.primer.ipay88.internal.PrimerIPay88Delegate
import io.primer.ipay88.internal.extension.toIPayIHPayment
import io.primer.ipay88.internal.extension.toIPayIHR

class NativeIPay88Activity : AppCompatActivity() {

    private val iPayIH by lazy { IPayIH.getInstance() }

    private val launcherIntent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        when (activityResult.resultCode) {
            RESULT_CANCELED -> setCancelledResult(IPayPaymentCancelledException())
            RESULT_OK -> {
                when (val state = IPayStateHolder.currentState) {
                    is IPayPaymentState.Cancelled -> {
                        setCancelledResult(IPayPaymentCancelledException())
                    }
                    is IPayPaymentState.ConnectionError -> {
                        setErrorResult(IPayConnectionErrorException())
                    }
                    is IPayPaymentState.Failed -> {
                        setErrorResult(
                            IPayPaymentFailedException(
                                state.transId,
                                state.refNo,
                                state.errorDescription
                            )
                        )
                    }
                    is IPayPaymentState.ReQuery -> {
                        reQuery(
                            intent.getSerializableExtra(INTENT_PARAMS_EXTRA_KEY) as IPay88LauncherParams
                        )
                    }
                    is IPayPaymentState.Success -> setResult(RESULT_OK)
                    else -> setErrorResult(IllegalStateException())
                }
            }
            else -> Unit
        }
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startCheckout(getLauncherParams())
    }

    private fun startCheckout(params: IPay88LauncherParams) {
        launcherIntent.launch(
            iPayIH.checkout(
                params.toIPayIHPayment(),
                this,
                PrimerIPay88Delegate(),
                IPayIH.PAY_METHOD_CREDIT_CARD
            )
        )
    }

    private fun reQuery(params: IPay88LauncherParams) {
        launcherIntent.launch(
            iPayIH.requery(
                params.toIPayIHR(),
                this,
                PrimerIPay88Delegate(),
            )
        )
    }

    private fun setErrorResult(exception: IllegalStateException) {
        val errorCode = getLauncherParams().errorCode
        setResult(errorCode, Intent().apply { putExtra(ERROR_KEY, exception) })
    }

    private fun setCancelledResult(exception: IllegalStateException) {
        setResult(RESULT_CANCELED, Intent().apply { putExtra(ERROR_KEY, exception) })
    }

    private fun getLauncherParams() =
        intent.getSerializableExtra(INTENT_PARAMS_EXTRA_KEY) as IPay88LauncherParams

    companion object {

        private const val INTENT_PARAMS_EXTRA_KEY = "INTENT_PARAMS_EXTRA"
        const val ERROR_KEY = "ERROR"

        fun getLaunchIntent(context: Context, params: IPay88LauncherParams) =
            Intent(context, NativeIPay88Activity::class.java).apply {
                putExtra(INTENT_PARAMS_EXTRA_KEY, params)
            }
    }
}
