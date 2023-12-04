package com.roman.lab4.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Country(
    @JsonProperty("name") var name: String?,
    @JsonProperty("capitalCity") var capital: String?,
    @JsonProperty("iso2Code") var code: String?,
    @JsonProperty("incomeLevel") var incomeLevel: Map<String, String?>?
) {
    constructor(country: CountryEntity?) : this(country?.name, country?.capital, country?.code, mapOf("value" to country?.incomeLevel))
}
