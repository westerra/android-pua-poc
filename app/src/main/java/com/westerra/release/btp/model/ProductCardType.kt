package com.westerra.release.btp.model

import com.google.gson.annotations.SerializedName

enum class ProductCardType(val type: String) {
    @SerializedName(value = "checking", alternate = ["Checking", "CHECKING"])
    CHECKING("checking"),

    @SerializedName(value = "savings", alternate = ["Savings", "SAVINGS"])
    SAVINGS("savings"),

    @SerializedName(
        value = "credit_card",
        alternate = ["Credit_Card", "CREDIT_CARD", "creditcard", "CreditCard", "CREDITCARD"],
    )
    CREDIT_CARD("credit_card"),

    @SerializedName(value = "cd", alternate = ["CD", "Cd"])
    CD("cd"),

    @SerializedName(value = "header", alternate = ["Header", "HEADER"])
    HEADER("header"),

    @SerializedName(value = "footer", alternate = ["Footer", "FOOTER"])
    FOOTER("footer"),
}
