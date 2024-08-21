package br.com.devaria.devameet.devameetkotlin.dtos.meet

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MeetCreateRequestDto (
    val name: String,
    val color : String
)