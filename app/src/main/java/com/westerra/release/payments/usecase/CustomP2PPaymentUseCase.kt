package com.westerra.release.payments.usecase

import com.backbase.android.client.paymentorderclient2.model.Identification
import com.backbase.android.client.paymentorderclient2.model.InitiateCounterpartyAccount
import com.backbase.android.client.paymentorderclient2.model.SchemeNames
import com.backbase.android.client.paymentorderclient2.model.SelectedContactDto
import com.backbase.android.retail.journey.payments.gen2_paymentorder_v2_client_2.PaymentOrderV2Client2PaymentServiceUseCase
import com.backbase.android.retail.journey.payments.model.PaymentOrder

class CustomP2PPaymentUseCase(paymentOrderV2Client2PaymentServiceUseCase: PaymentOrderV2Client2PaymentServiceUseCase) : CustomPaymentUseCaseBase(paymentOrderV2Client2PaymentServiceUseCase) {

    override fun createInitiateCounterpartyAccount(
        paymentOrder: PaymentOrder,
    ): InitiateCounterpartyAccount {
        return InitiateCounterpartyAccount {
            identification = Identification {
                identification = paymentOrder.toParty.identifications[0].identification
                schemeName = SchemeNames.BBAN
            }
            selectedContact = SelectedContactDto {
                contactId = paymentOrder.toParty.id
            }
        }
    }
}
