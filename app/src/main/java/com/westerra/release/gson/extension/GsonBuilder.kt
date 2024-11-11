package com.westerra.release.gson.extension

import com.backbase.android.client.paymentorderclient2.model.Schedule.Every
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.westerra.release.gson.adapter.BackBaseEveryAdapter
import com.westerra.release.gson.adapter.LocalDateAdapter
import java.time.LocalDate

fun GsonBuilder.createPaymentsGson(): Gson {
    return GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .registerTypeAdapter(Every::class.java, BackBaseEveryAdapter())
        .create()
}
