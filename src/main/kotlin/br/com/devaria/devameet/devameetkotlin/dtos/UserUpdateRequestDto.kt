package br.com.devaria.devameet.devameetkotlin.dtos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserUpdateRequestDto(
    val name: String,
    val avatar: String
)