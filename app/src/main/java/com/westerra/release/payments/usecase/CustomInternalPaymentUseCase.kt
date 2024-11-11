package com.westerra.release.payments.usecase

import com.backbase.android.client.paymentorderclient2.model.Identification
import com.backbase.android.client.paymentorderclient2.model.InitiateCounterpartyAccount
import com.backbase.android.client.paymentorderclient2.model.SchemeNames
import com.backbase.android.retail.journey.payments.model.PaymentOrder

class CustomInternalPaymentUseCase : CustomPaymentUseCaseBase() {

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
