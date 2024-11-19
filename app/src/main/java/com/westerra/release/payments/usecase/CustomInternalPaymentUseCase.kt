package com.westerra.release.payments.usecase

import com.backbase.android.client.paymentorderclient2.model.Identification
import com.backbase.android.client.paymentorderclient2.model.InitiateCounterpartyAccount
import com.backbase.android.client.paymentorderclient2.model.SchemeNames
import com.backbase.android.retail.journey.payments.gen2_paymentorder_v2_client_2.PaymentOrderV2Client2PaymentServiceUseCase
import com.backbase.android.retail.journey.payments.model.PaymentOrder

class CustomInternalPaymentUseCase(paymentOrderV2Client2PaymentServiceUseCase: PaymentOrderV2Client2PaymentServiceUseCase) : CustomPaymentUseCaseBase(
    paymentOrderV2Client2PaymentServiceUseCase
) {

    override fun createInitiateCounterpartyAccount(
        paymentOrder: PaymentOrder,
    ): InitiateCounterpartyAccount {
        return InitiateCounterpartyAccount {
            identification = Identification {
                identification = paymentOrder.toParty.id
                schemeName = SchemeNames.ID
            }
        }
    }
}
