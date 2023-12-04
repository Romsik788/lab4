package com.roman.lab4.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country")
data class CountryEntity(
    @PrimaryKey var name: String,
    var capital: String?,
    var code: String?,
    var incomeLevel: String?
) {
    constructor(country: Country?) : this(country?.name.toString(), country?.capital, country?.code, country?.incomeLevel?.get("value"))
}