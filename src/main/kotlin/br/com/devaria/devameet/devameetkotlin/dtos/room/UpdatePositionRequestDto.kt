package br.com.devaria.devameet.devameetkotlin.dtos.room

data class UpdatePositionRequestDto(
    val userId: Long,
    val clientId: String,
    var link: String,
    val x: Int = 0,
    val y: Int = 0,
    val orientation: String = "",
    val muted: Boolean = false,
)
