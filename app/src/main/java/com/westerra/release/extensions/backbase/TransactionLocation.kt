package com.westerra.release.extensions.backbase

import com.backbase.android.client.transactionclient2.model.TransactionLocation
import com.backbase.android.retail.journey.accounts_and_transactions.transactions.models.GeoCoordinates
import java.math.BigDecimal

fun TransactionLocation.isValid(): Boolean {
    return latitude != null && latitude != BigDecimal.ZERO &&
        longitude != null && longitude != BigDecimal.ZERO
}

fun TransactionLocation.toGeoCoordinates(): GeoCoordinates? {
    val lat = latitude
    val lon = longitude
    if (lat == null || lon == null || !isValid()) return null
    return GeoCoordinates(lat, lon)
}
