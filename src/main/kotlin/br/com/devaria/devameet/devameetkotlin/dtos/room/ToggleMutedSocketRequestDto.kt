package br.com.devaria.devameet.devameetkotlin.dtos.room

data class ToggleMutedSocketRequestDto(
    val userId: Long = 0,
    var link: String = "",
    var muted: Boolean = false,
)
