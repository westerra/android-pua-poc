package com.westerra.release

import android.util.Log
import com.backbase.android.clients.common.BigDecimalNumericJsonAdapter
import com.backbase.android.clients.common.MoshiResponseBodyParser
import com.backbase.android.clients.common.ResponseBodyParser
import com.backbase.android.clients.common.base64Adapter
import com.backbase.android.clients.common.dateAdapter
import com.backbase.android.clients.common.uuidAdapter
import com.backbase.android.identity.journey.authentication.AuthenticationUseCase
import com.backbase.android.identity.journey.authentication.identity_auth_client_1.IdentityAuthClient1AuthenticationUseCase
import com.backbase.android.retail.journey.app.common.COMMON_API_ROOT_QUALIFIER
import com.backbase.android.retail.journey.cardsmanagement.CardsManagementJourneyScope
import com.backbase.android.retail.journey.cardsmanagement.usecase.TravelDestinationsUseCase
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.westerra.release.cards.ModelBankTravelDestinationUseCase
import java.math.BigDecimal
import java.time.OffsetDateTime
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope
import org.koin.dsl.ModuleDeclaration
import org.koin.dsl.bind

/**
 * Returns [ModuleDeclaration] containing additional dependencies required by ModelBank applications.
 *
 * @return [ModuleDeclaration] which could be loaded inside a Koin module.
 *
 * Some dates come in a format that is not an ISO standard from the backend. That format describes
 * timezones as [+-]HHmm$.
 *
 * This workaround converts the string's timezone to [+-]HH:mm$. When the backend fixes this issue,
 * the current workaround will not break the app, though it's advisable to remove it.
 *
 * The bug this code works around is known to be present only in the following spec: message-client-api-v4.0.7.yaml
 */
@Suppress("FunctionName") // factory method
internal fun WesterraAdditionalDependencies(): ModuleDeclaration = {
    //region Single dependencies
    single {
        Moshi.Builder()
            .add(base64Adapter)
            .add(dateAdapter)
            .add(uuidAdapter)
            .add(BigDecimal::class.java, BigDecimalNumericJsonAdapter)
            .add(KotlinJsonAdapterFactory())
            .add(
                object {
                    @FromJson
                    fun fromJson(string: String) = OffsetDateTime.parse(
                        string.replace(
                            Regex("(?<=[+-])(\\d{2})(\\d{2})$"),
                            "$1:$2",
                        ),
                    )

                    @ToJson
                    fun toJson(value: OffsetDateTime) = value.toString()
                },
            )
            .build()
    }
    //endregion

    //region Factory dependencies
    factory<ResponseBodyParser> { MoshiResponseBodyParser(get()) }
    //endregion

    scope<CardsManagementJourneyScope> {
        scoped<TravelDestinationsUseCase> {
            ModelBankTravelDestinationUseCase(context = androidContext())
        }
    }

    single {
        IdentityAuthClient1AuthenticationUseCase(
            get(),
            get(),
            authenticationDeregistrationListener = {
                Log.i("WesterraAdditionalDependencies", "User de-registration event")
            },
        )
    } bind AuthenticationUseCase::class
}

internal fun Scope.apiRoot() = get<String>(COMMON_API_ROOT_QUALIFIER)
