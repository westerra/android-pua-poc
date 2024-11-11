package com.westerra.release.payments.usecase

import com.backbase.android.client.paymentorderclient2.model.Identification
import com.backbase.android.client.paymentorderclient2.model.InitiateCounterpartyAccount
import com.backbase.android.client.paymentorderclient2.model.SchemeNames
import com.backbase.android.client.paymentorderclient2.model.SelectedContactDto
import com.backbase.android.retail.journey.payments.model.PaymentOrder

class CustomP2PPaymentUseCase : CustomPaymentUseCaseBase() {

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
