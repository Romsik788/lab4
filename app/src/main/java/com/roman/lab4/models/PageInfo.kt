package com.roman.lab4.models

import com.fasterxml.jackson.annotation.JsonProperty

data class PageInfo(
    val page: Int,
    val pages: Int,
    @JsonProperty("per_page") val perPage: String,
    val total: Int
)
