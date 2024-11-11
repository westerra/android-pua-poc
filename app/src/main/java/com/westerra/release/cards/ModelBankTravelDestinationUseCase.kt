package com.westerra.release.cards

import android.content.Context
import com.backbase.android.clients.common.MoshiResponseBodyParser
import com.backbase.android.retail.journey.cardsmanagement.CallState
import com.backbase.android.retail.journey.cardsmanagement.dtos.TravelDestination
import com.backbase.android.retail.journey.cardsmanagement.dtos.TravelDestinationRegion
import com.backbase.android.retail.journey.cardsmanagement.usecase.GetTravelDestinationsParams
import com.backbase.android.retail.journey.cardsmanagement.usecase.TravelDestinationsUseCase
import com.backbase.android.utils.net.response.Response
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * An implementation of [TravelDestinationsUseCase] based on local files.
 *
 * @property context The application [Context] object to read a local file.
 *
 * TODO: [MAINT-15195] Remove when tagged ticket is done.
 */
class ModelBankTravelDestinationUseCase(
    private val context: Context,
) : TravelDestinationsUseCase {
    override suspend fun getTravelDestinations(
        params: GetTravelDestinationsParams,
    ): CallState<out List<TravelDestination>> {
        return try {
            val moshi =
                Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            val jsonContent =
                context.resources.openRawResource(params.resDestinationsFileId)
                    .bufferedReader()
                    .use { it.readText() }

            val listType =
                Types.newParameterizedType(List::class.java, TravelDestinationJsonData::class.java)

            val moshiResponseBodyParser = MoshiResponseBodyParser(moshi)
            val result =
                moshiResponseBodyParser.parse<List<TravelDestinationJsonData>>(
                    jsonContent,
                    listType,
                )

            val travelDestinationList = mapToTravelDestinationList(result)

            params.destinationIds?.let {
                val travelDestinationListFiltered =
                    travelDestinationList.filterByCode(params.destinationIds!!.flatten())
                if (travelDestinationListFiltered.isEmpty()) {
                    CallState.Empty
                } else {
                    CallState.Success(travelDestinationListFiltered)
                }
            } ?: CallState.Success(travelDestinationList)
        } catch (ex: Exception) {
            CallState.Error(
                Response(
                    RuntimeException(
                        "The content of ${params.resDestinationsFileId} file cannot be parsed",
                    ),
                ),
            )
        }
    }

    private fun mapToTravelDestinationList(
        list: List<TravelDestinationJsonData>,
    ): List<TravelDestination> {
        return list.map {
            TravelDestination {
                this.code = it.code
                this.name = it.name
                it.regions?.let { regions ->
                    this.regions =
                        regions.map {
                            TravelDestinationRegion {
                                code = it.code
                                name = it.name
                            }
                        }
                }
            }
        }
    }
}

@JsonClass(generateAdapter = true)
data class TravelDestinationJsonData internal constructor(
    @Json(name = "code")
    val code: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "regions")
    val regions: List<TravelDestinationRegionJsonData>?,
)

@JsonClass(generateAdapter = true)
data class TravelDestinationRegionJsonData internal constructor(
    @Json(name = "code")
    val code: String,
    @Json(name = "name")
    val name: String,
)

internal fun List<TravelDestination>.filterByCode(
    destinationsIds: List<String>,
): List<TravelDestination> = this.filter {
    it.code in destinationsIds || it.regions?.any { region ->
        region.code in destinationsIds
    } == true
}
    .map {
        TravelDestination {
            code = it.code
            name = it.name
            regions =
                it.regions?.filter {
                    it.code in destinationsIds
                }
        }
    }

internal fun Map<String, List<String>?>.flatten() = this.flatMap {
    mutableListOf<String>().apply {
        it.value?.let { regions ->
            addAll(regions)
        } ?: add(it.key)
    }
}
