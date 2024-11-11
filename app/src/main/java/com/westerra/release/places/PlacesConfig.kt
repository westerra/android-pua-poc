package com.westerra.release.places

import com.backbase.android.retail.journey.places.PlacesConfiguration
import com.backbase.android.retail.journey.places.details.PlaceDetailsConfiguration
import com.backbase.android.retail.journey.places.map.PlacesMapScreenConfiguration
import com.backbase.android.retail.journey.places.search.PlacesSearchConfiguration
import com.westerra.release.R
import com.westerra.release.constants.Constants.DENVER_LATITUDE
import com.westerra.release.constants.Constants.DENVER_LONGITUDE
import com.westerra.release.extensions.toDeferredText
import java.math.BigDecimal

object PlacesConfig {
    operator fun invoke(): PlacesConfiguration {
        return PlacesConfiguration {
            placeDetailsConfiguration = PlaceDetailsConfiguration {
            }
            placesSearchConfiguration = PlacesSearchConfiguration {
            }
            placesMapScreenConfiguration = PlacesMapScreenConfiguration {
                defaultLatitude = BigDecimal(DENVER_LATITUDE)
                defaultLongitude = BigDecimal(DENVER_LONGITUDE)
            }
                .buildUpon()
                .apply {
                    googlePlaceAPIKey = R.string.google_places_api_key.toDeferredText()
                }.build()
        }
    }
}
