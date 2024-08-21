package br.com.devaria.devameet.devameetkotlin.dtos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class LoginResponseDto(val token: String, val name: String, val email: String)